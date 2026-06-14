package com.cambofreelance.webbackend.registry;

import com.cambofreelance.webbackend.caches.ResponseCodeRedisCache;
import com.cambofreelance.webbackend.caches.ResponseManagerCache;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.ResponseCodeDto;
import com.cambofreelance.webbackend.entities.ResponseCodeEntity;
import com.cambofreelance.webbackend.entities.RoleEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.repository.PermissionRepository;
import com.cambofreelance.webbackend.repository.ResponseCodeRepository;
import com.cambofreelance.webbackend.repository.RoleRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiMigrateRegistry {

    private final ResponseCodeRepository responseCodeRepository;
    private final ResponseCodeRedisCache responseCodeRedisCache;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public void loadComponentInit() {
        log.info("Loading component ...");
        this.loadResponseCode();
        log.info("Init Seeding admin user");
        this.seedUserAdmin();
        log.info("Seeding admin user completed.");
    }

    public void loadResponseCode() {
        Thread threadLoadResponseCode = new Thread(() -> {
            try {
                List<ResponseCodeEntity> responseCodeList = responseCodeRepository.findByStatus(
                    Constants.STATUS_ACTIVE);
                List<ResponseCodeDto> responseCodeDtoList = responseCodeList.stream()
                    .map(entity -> new ResponseCodeDto(
                        entity.getId(),
                        entity.getCode(),
                        entity.getHttpStatus(),
                        entity.getKey(),
                        entity.getType(),
                        entity.getDescription(),
                        entity.getMessageEn(),
                        entity.getMessageKm(),
                        entity.getMessageCn(),
                        entity.getStatus()
                    ))
                    .toList();
                ResponseManagerCache.initRespCodeCache(responseCodeDtoList);
                responseCodeRedisCache.initRespCodeCache(responseCodeDtoList);
            } catch (Exception e) {
                log.error("Error loading API routes: {}", e.getMessage(), e);
            }
        });
        threadLoadResponseCode.start();
    }


    private void seedUserAdmin() {
        var roles = roleRepository.findAllByStatus(Constants.STATUS_ACTIVE);
        if (!roles.isEmpty()) {
            log.info("Roles already exist, skipping role seed.");
        } else {
            RoleEntity adminRole = buildRole("ADMIN",        "Administrator", "lower_conversion", 1, "Administrator role with full permissions");
            RoleEntity userRole  = buildRole("USER",         "User",          "lower_conversion", 2, "User role with limited permissions");
            RoleEntity creatorRole = buildRole("CREATOR_USER", "Creator User",  "lower_conversion", 3, "Creator User role");
            RoleEntity publicRole  = buildRole("PUBLIC_USER",  "Public User",   "lower_conversion", 4, "Public User role");

            // Assign all existing permissions to the ADMIN role
            var allPermissions = new HashSet<>(permissionRepository.findAll());
            adminRole.setPermissions(allPermissions);
            log.info("Assigning {} permissions to ADMIN role", allPermissions.size());

            roleRepository.save(adminRole);
            roleRepository.save(userRole);
            roleRepository.save(creatorRole);
            roleRepository.save(publicRole);
        }

        var adminUserOpt = userRepository.findByUsernameAndStatus("super.admin", Constants.STATUS_ACTIVE);
        if (adminUserOpt.isPresent()) {
            return;
        }

        UserEntity adminUser = new UserEntity();
        adminUser.setUserId(UUID.randomUUID().toString());
        adminUser.setUsername("super.admin");
        adminUser.setPassword(bCryptPasswordEncoder.encode("Admin@123"));
        adminUser.setEmail("super.admin@gmail.com");
        adminUser.setApplicationId("SYSTEM");
        adminUser.setPhoneNumber("0962505045");
        adminUser.setStatus(Constants.STATUS_ACTIVE);
        adminUser.setIsForceChangePassword("N");
        adminUser.setInvalidPasswordCount(0);
        adminUser.setInvalidOtpCount(0);
        adminUser.setCreatedAt(new Date());
        adminUser.setCreatedBy("SYSTEM");
        // Assign all roles to the super.admin user
        adminUser.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(adminUser);
        log.info("Seeded default admin user: super.admin / Admin@123");
    }

    private RoleEntity buildRole(String code, String name, String convention, int level, String description) {
        RoleEntity role = new RoleEntity();
        role.setId(UUID.randomUUID().toString());
        role.setCode(code);
        role.setName(name);
        role.setConvention(convention);
        role.setLevel(level);
        role.setDescription(description);
        role.setStatus(Constants.STATUS_ACTIVE);
        role.setCreatedBy(Constants.SYSTEM);
        return role;
    }

}