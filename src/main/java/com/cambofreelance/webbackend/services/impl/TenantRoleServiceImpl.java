package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.TenantAssignablePermissions;
import com.cambofreelance.webbackend.dto.request.TenantRoleCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRoleUpdateRequest;
import com.cambofreelance.webbackend.dto.response.TenantRoleResponse;
import com.cambofreelance.webbackend.entities.PermissionEntity;
import com.cambofreelance.webbackend.entities.RoleEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.PermissionRepository;
import com.cambofreelance.webbackend.repository.RoleRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.TenantRoleService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantRoleServiceImpl implements TenantRoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public List<TenantRoleResponse> listForTenant(String tenantId) {
        return roleRepository.findByTenantIdAndStatusNot(tenantId, Constants.STATUS_DELETE).stream()
            .map(TenantRoleResponse::from)
            .toList();
    }

    @Override
    @Transactional
    public TenantRoleResponse create(String tenantId, TenantRoleCreateRequest request) {
        Set<PermissionEntity> permissions = resolveAllowedPermissions(request.getPermissions());

        RoleEntity role = new RoleEntity();
        role.setId(UUID.randomUUID().toString());
        role.setCode("TR_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setTenantId(tenantId);
        role.setStatus(Constants.STATUS_ACTIVE);
        role.setCreatedAt(new Date());
        role.setPermissions(permissions);

        return TenantRoleResponse.from(roleRepository.save(role));
    }

    @Override
    @Transactional
    public TenantRoleResponse update(String tenantId, String roleId, TenantRoleUpdateRequest request) {
        RoleEntity role = findOwnedRole(tenantId, roleId);
        if (request.getName() != null) role.setName(request.getName());
        if (request.getDescription() != null) role.setDescription(request.getDescription());
        if (request.getPermissions() != null) {
            role.setPermissions(resolveAllowedPermissions(request.getPermissions()));
        }
        role.setUpdatedAt(new Date());
        return TenantRoleResponse.from(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void delete(String tenantId, String roleId) {
        RoleEntity role = findOwnedRole(tenantId, roleId);
        role.setStatus(Constants.STATUS_DELETE);
        role.setUpdatedAt(new Date());
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void assignToUser(String tenantId, String userId, String roleId) {
        RoleEntity role = findOwnedRole(tenantId, roleId);
        UserEntity user = findTenantUser(tenantId, userId);
        Set<RoleEntity> roles = new HashSet<>(user.getRoles());
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFromUser(String tenantId, String userId, String roleId) {
        RoleEntity role = findOwnedRole(tenantId, roleId);
        UserEntity user = findTenantUser(tenantId, userId);
        Set<RoleEntity> roles = new HashSet<>(user.getRoles());
        roles.remove(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<TenantRoleResponse> seedDefaultRoles(String tenantId) {
        record DefaultRole(String name, String description, Set<String> codes) {}
        List<DefaultRole> defaults = List.of(
            new DefaultRole("Administrator",
                "Full access to all loan management features",
                TenantAssignablePermissions.ROLE_ADMINISTRATOR),
            new DefaultRole("Loan Officer",
                "Customer management, loan applications and collection follow-up",
                TenantAssignablePermissions.ROLE_LOAN_OFFICER),
            new DefaultRole("Approver",
                "Loan approval, disbursement authorization and reporting",
                TenantAssignablePermissions.ROLE_APPROVER),
            new DefaultRole("Cashier",
                "Payment collection and receipt management",
                TenantAssignablePermissions.ROLE_CASHIER)
        );

        List<String> existingNames = roleRepository.findByTenantIdAndStatusNot(tenantId, Constants.STATUS_DELETE)
            .stream().map(RoleEntity::getName).toList();

        List<TenantRoleResponse> created = new ArrayList<>();
        for (DefaultRole def : defaults) {
            if (existingNames.contains(def.name())) continue;
            RoleEntity role = new RoleEntity();
            role.setId(UUID.randomUUID().toString());
            role.setCode("TR_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            role.setName(def.name());
            role.setDescription(def.description());
            role.setTenantId(tenantId);
            role.setStatus(Constants.STATUS_ACTIVE);
            role.setCreatedAt(new Date());
            role.setPermissions(resolveAllowedPermissions(def.codes()));
            created.add(TenantRoleResponse.from(roleRepository.save(role)));
        }
        return created;
    }

    private Set<PermissionEntity> resolveAllowedPermissions(Set<String> requestedCodes) {
        if (requestedCodes == null || requestedCodes.isEmpty()) {
            return new HashSet<>();
        }
        List<String> notAllowed = new ArrayList<>(requestedCodes);
        notAllowed.removeAll(TenantAssignablePermissions.ALL);
        if (!notAllowed.isEmpty()) {
            throw new AppException("PERMISSION_NOT_ALLOWED",
                "These permissions cannot be assigned to a tenant role: " + notAllowed);
        }
        return new HashSet<>(permissionRepository.findAllByCodeIn(new ArrayList<>(requestedCodes)));
    }

    private RoleEntity findOwnedRole(String tenantId, String roleId) {
        return roleRepository.findByIdAndTenantId(roleId, tenantId)
            .orElseThrow(() -> new AppException("ROLE_NOT_FOUND", "Role not found for this tenant: " + roleId));
    }

    private UserEntity findTenantUser(String tenantId, String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException("USER_NOT_FOUND", "User not found: " + userId));
        if (!tenantId.equals(user.getTenantId())) {
            throw new AppException("USER_NOT_IN_TENANT", "User does not belong to this tenant");
        }
        return user;
    }
}
