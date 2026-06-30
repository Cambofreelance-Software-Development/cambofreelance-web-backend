package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.SubscriptionCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantRegistrationRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUserCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUserUpdateRequest;
import com.cambofreelance.webbackend.dto.response.TenantDashboardResponse;
import com.cambofreelance.webbackend.dto.response.TenantRegistrationResponse;
import com.cambofreelance.webbackend.dto.response.TenantResponse;
import com.cambofreelance.webbackend.dto.response.UserProfileResponse;
import com.cambofreelance.webbackend.entities.RoleEntity;
import com.cambofreelance.webbackend.entities.TenantEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.multitenancy.TenantSchemaProvisioningService;
import com.cambofreelance.webbackend.repository.RoleRepository;
import com.cambofreelance.webbackend.repository.TenantRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.RefreshTokenService;
import com.cambofreelance.webbackend.services.SubscriptionService;
import com.cambofreelance.webbackend.services.TenantService;
import com.cambofreelance.webbackend.services.UsageMetricsService;
import com.cambofreelance.webbackend.services.UserService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private static final Set<String> ALLOWED_STATUS_UPDATES =
        Set.of(Constants.STATUS_ACTIVE, Constants.STATUS_SUSPENDED, Constants.STATUS_EXPIRED);

    private static final Set<String> ALLOWED_USER_STATUS_UPDATES =
        Set.of(Constants.STATUS_ACTIVE, Constants.STATUS_LEAVE);

    private static final long EXPIRING_SOON_WINDOW_MS = 30L * 24 * 60 * 60 * 1000;

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TenantSchemaProvisioningService tenantSchemaProvisioningService;
    private final SubscriptionService subscriptionService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsageMetricsService usageMetricsService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Page<TenantResponse> list(String search, String status, String tenantType, int page, int size) {
        Specification<TenantEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("status"), Constants.STATUS_DELETE));

            if (search != null && !search.isBlank()) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("code")), like),
                    cb.like(cb.lower(root.get("companyName")), like)
                ));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status.trim()));
            }
            if (tenantType != null && !tenantType.isBlank()) {
                predicates.add(cb.equal(root.get("tenantType"), tenantType.trim()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.asc("name"));
        return tenantRepository.findAll(spec, PageRequest.of(page, size, sort)).map(TenantResponse::from);
    }

    @Override
    public TenantResponse getById(String id) {
        return TenantResponse.from(findOrThrow(id));
    }

    @Override
    @Transactional
    public TenantResponse create(TenantCreateRequest request) {
        if (tenantRepository.existsByCode(request.getCode())) {
            throw new AppException("TENANT_CODE_EXISTS", "Tenant code '" + request.getCode() + "' already exists");
        }

        TenantEntity entity = new TenantEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(request.getCode().trim().toUpperCase());
        entity.setName(request.getName());
        entity.setTenantType(request.getTenantType());
        entity.setDescription(request.getDescription());
        entity.setLogoUrl(request.getLogoUrl());
        entity.setPrimaryColor(request.getPrimaryColor());
        entity.setSecondaryColor(request.getSecondaryColor());
        entity.setCompanyName(request.getCompanyName());
        entity.setCompanyAddress(request.getCompanyAddress());
        entity.setCompanyEmail(request.getCompanyEmail());
        entity.setCompanyPhone(request.getCompanyPhone());
        entity.setWebsite(request.getWebsite());
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedAt(new Date());

        TenantEntity saved = tenantRepository.save(entity);
        tenantSchemaProvisioningService.provisionSchema(saved.getId());
        return TenantResponse.from(saved);
    }

    @Override
    @Transactional
    public TenantResponse update(String id, TenantUpdateRequest request) {
        TenantEntity entity = findOrThrow(id);

        if (request.getName() != null) entity.setName(request.getName());
        if (request.getTenantType() != null) entity.setTenantType(request.getTenantType());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) entity.setLogoUrl(request.getLogoUrl());
        if (request.getPrimaryColor() != null) entity.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) entity.setSecondaryColor(request.getSecondaryColor());
        if (request.getCompanyName() != null) entity.setCompanyName(request.getCompanyName());
        if (request.getCompanyAddress() != null) entity.setCompanyAddress(request.getCompanyAddress());
        if (request.getCompanyEmail() != null) entity.setCompanyEmail(request.getCompanyEmail());
        if (request.getCompanyPhone() != null) entity.setCompanyPhone(request.getCompanyPhone());
        if (request.getWebsite() != null) entity.setWebsite(request.getWebsite());
        entity.setUpdatedAt(new Date());

        return TenantResponse.from(tenantRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(String id) {
        TenantEntity entity = findOrThrow(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        tenantRepository.save(entity);
    }

    @Override
    @Transactional
    public TenantResponse updateStatus(String id, String status) {
        if (!ALLOWED_STATUS_UPDATES.contains(status)) {
            throw new AppException("TENANT_INVALID_STATUS", "Status must be one of ACT, SUS, EXP");
        }
        TenantEntity entity = findOrThrow(id);
        entity.setStatus(status);
        entity.setUpdatedAt(new Date());
        return TenantResponse.from(tenantRepository.save(entity));
    }

    @Override
    public TenantDashboardResponse getDashboardStats() {
        long active = tenantRepository.countByStatus(Constants.STATUS_ACTIVE);
        long suspended = tenantRepository.countByStatus(Constants.STATUS_SUSPENDED);
        long expired = tenantRepository.countByStatus(Constants.STATUS_EXPIRED);
        long total = active + suspended + expired;

        Date now = new Date();
        Date soonCutoff = new Date(now.getTime() + EXPIRING_SOON_WINDOW_MS);
        List<TenantResponse> expiringSoon = tenantRepository
            .findByStatusAndPlanExpiredDateBetween(Constants.STATUS_ACTIVE, now, soonCutoff)
            .stream().map(TenantResponse::from).toList();
        List<TenantResponse> recent = tenantRepository
            .findTop5ByStatusNotOrderByCreatedAtDesc(Constants.STATUS_DELETE)
            .stream().map(TenantResponse::from).toList();

        return TenantDashboardResponse.builder()
            .totalTenants(total)
            .activeTenants(active)
            .suspendedTenants(suspended)
            .expiredTenants(expired)
            .totalAssignedUsers(userRepository.countByTenantIdNotNullAndStatus(Constants.STATUS_ACTIVE))
            .tenantsExpiringSoon(expiringSoon)
            .recentTenants(recent)
            .build();
    }

    @Override
    public Page<UserProfileResponse> listTenantUsers(String tenantId, String search, String status, int page, int size) {
        findOrThrow(tenantId);
        Specification<UserEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("tenantId"), tenantId));
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("username")), like),
                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(root.get("phoneNumber"), "%" + search.trim() + "%")
                ));
            }
            if (StringUtils.hasText(status)) {
                predicates.add(cb.equal(root.get("status"), status.trim()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return userRepository.findAll(spec, PageRequest.of(page, size, sort)).map(userService::toProfileResponse);
    }

    @Override
    @Transactional
    public UserProfileResponse assignUser(String tenantId, String userId) {
        findOrThrow(tenantId);
        assertSeatAvailable(tenantId);
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException("USER_NOT_FOUND", "User not found: " + userId));
        user.setTenantId(tenantId);
        user.setUpdatedAt(new Date());
        return userService.toProfileResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void removeUser(String tenantId, String userId) {
        UserEntity user = findTenantOwnedUser(tenantId, userId);
        user.setTenantId(null);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserProfileResponse createTenantUser(String tenantId, TenantUserCreateRequest request) {
        TenantEntity tenant = findOrThrow(tenantId);
        assertSeatAvailable(tenantId);

        String username = tenant.getCode() + "." + request.getUsername().trim();
        if (userRepository.findByUsernameAndStatus(username, Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException("USERNAME_ALREADY_EXIST", "Username already exists");
        }
        if (userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException("EMAIL_ALREADY_EXIST", "Email already in use");
        }
        if (StringUtils.hasText(request.getPhoneNumber())
            && userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException("PHONE_ALREADY_EXIST", "Phone number already in use");
        }

        Date now = new Date();
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setUserType(Constants.USER);
        user.setStatus(Constants.STATUS_ACTIVE);
        user.setTenantId(tenantId);
        user.setCreatedAt(now);
        userRepository.save(user);

        return userService.toProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateTenantUser(String tenantId, String userId, TenantUserUpdateRequest request) {
        UserEntity user = findTenantOwnedUser(tenantId, userId);

        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(user.getEmail())) {
            userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException("EMAIL_ALREADY_EXIST", "Email already in use"); });
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPhoneNumber()) && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException("PHONE_ALREADY_EXIST", "Phone number already in use"); });
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }
        user.setUpdatedAt(new Date());
        userRepository.save(user);

        return userService.toProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateTenantUserStatus(String tenantId, String userId, String status) {
        if (!ALLOWED_USER_STATUS_UPDATES.contains(status)) {
            throw new AppException("INVALID_STATUS", "Status must be one of ACT, LEA");
        }
        UserEntity user = findTenantOwnedUser(tenantId, userId);
        user.setStatus(status);
        user.setUpdatedAt(new Date());
        userRepository.save(user);

        if (!Constants.STATUS_ACTIVE.equals(status)) {
            refreshTokenService.revokeAllByUserId(userId);
        }
        return userService.toProfileResponse(user);
    }

    private void assertSeatAvailable(String tenantId) {
        var activeUsers = usageMetricsService.getUsageForTenant(tenantId).getActiveUsers();
        Integer limit = activeUsers.getLimit();
        if (limit != null && activeUsers.getCurrent() >= limit) {
            throw new AppException("USER_LIMIT_REACHED",
                "This tenant has reached its plan's user limit (" + limit + "). Upgrade the package to add more users.");
        }
    }

    private UserEntity findTenantOwnedUser(String tenantId, String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException("USER_NOT_FOUND", "User not found: " + userId));
        if (!tenantId.equals(user.getTenantId())) {
            throw new AppException("USER_NOT_IN_TENANT", "User does not belong to this tenant");
        }
        return user;
    }

    @Override
    public void assertActiveForLogin(String tenantId) {
        TenantEntity tenant = tenantRepository.findById(tenantId).orElse(null);
        if (tenant == null || Constants.STATUS_DELETE.equals(tenant.getStatus())) {
            throw new AppException("TENANT_NOT_FOUND", "Tenant not found or inactive");
        }
        if (Constants.STATUS_SUSPENDED.equals(tenant.getStatus())) {
            throw new AppException("TENANT_SUSPENDED", "Tenant account is suspended. Please contact support.");
        }
        if (Constants.STATUS_EXPIRED.equals(tenant.getStatus())) {
            throw new AppException("TENANT_EXPIRED", "Tenant subscription has expired. Please renew your plan.");
        }
    }

    @Override
    @Transactional
    public TenantRegistrationResponse register(TenantRegistrationRequest request) {
        if (tenantRepository.existsByCode(request.getCode())) {
            throw new AppException("TENANT_CODE_EXISTS", "Tenant code '" + request.getCode() + "' already exists");
        }
        if (userRepository.findByUsernameAndStatus(request.getUsername(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException("USERNAME_ALREADY_EXIST", "Username already exists");
        }
        if (userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException("EMAIL_ALREADY_EXIST", "Email already in use");
        }
        if (StringUtils.hasText(request.getPhoneNumber())
            && userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException("PHONE_ALREADY_EXIST", "Phone number already in use");
        }

        Date now = new Date();

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setUserType(Constants.USER);
        user.setStatus(Constants.STATUS_ACTIVE);
        user.setCreatedAt(now);
        userRepository.save(user);

        TenantEntity tenant = new TenantEntity();
        tenant.setId(UUID.randomUUID().toString());
        tenant.setCode(request.getCode().trim().toUpperCase());
        tenant.setName(request.getName());
        tenant.setTenantType(request.getTenantType());
        tenant.setDescription(request.getDescription());
        tenant.setLogoUrl(request.getLogoUrl());
        tenant.setPrimaryColor(request.getPrimaryColor());
        tenant.setSecondaryColor(request.getSecondaryColor());
        tenant.setCompanyName(request.getCompanyName());
        tenant.setCompanyAddress(request.getCompanyAddress());
        tenant.setCompanyEmail(request.getCompanyEmail());
        tenant.setCompanyPhone(request.getCompanyPhone());
        tenant.setWebsite(request.getWebsite());
        tenant.setRequestedByUserId(user.getUserId());
        tenant.setStatus(Constants.STATUS_PENDING);
        tenant.setCreatedAt(now);
        tenantRepository.save(tenant);

        SubscriptionCreateRequest subRequest = new SubscriptionCreateRequest();
        subRequest.setPackageId(request.getPackageId());
        subRequest.setBillingCycle(request.getBillingCycle());
        subRequest.setAmount(request.getAmount());
        subRequest.setCurrency(request.getCurrency());
        subRequest.setAutoRenewal(request.getAutoRenewal());
        subscriptionService.createPending(tenant.getId(), subRequest);

        return TenantRegistrationResponse.builder()
            .tenantId(tenant.getId())
            .status(tenant.getStatus())
            .message("Registration submitted. An administrator will review and approve your account.")
            .build();
    }

    @Override
    public Page<TenantResponse> listPending(int page, int size) {
        Specification<TenantEntity> spec = (root, query, cb) -> cb.equal(root.get("status"), Constants.STATUS_PENDING);
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        return tenantRepository.findAll(spec, PageRequest.of(page, size, sort)).map(TenantResponse::from);
    }

    @Override
    @Transactional
    public TenantResponse approve(String tenantId) {
        TenantEntity tenant = findOrThrow(tenantId);
        if (!Constants.STATUS_PENDING.equals(tenant.getStatus())) {
            throw new AppException("TENANT_NOT_PENDING", "Only a pending tenant registration can be approved");
        }

        String pendingSubscriptionId = subscriptionService.listHistoryForTenant(tenantId).stream()
            .filter(s -> Constants.STATUS_PENDING.equals(s.getStatus()))
            .findFirst()
            .orElseThrow(() -> new AppException("NO_PENDING_SUBSCRIPTION", "No pending subscription found for this tenant"))
            .getId();
        subscriptionService.activate(pendingSubscriptionId);

        if (StringUtils.hasText(tenant.getRequestedByUserId())) {
            userRepository.findById(tenant.getRequestedByUserId()).ifPresent(user -> {
                user.setTenantId(tenant.getId());
                RoleEntity tenantAdmin = roleRepository.findByCode("TENANT_ADMIN")
                    .orElseThrow(() -> new AppException("TENANT_ADMIN_ROLE_MISSING",
                        "TENANT_ADMIN role not found — cannot grant tenant admin access on approval"));
                Set<RoleEntity> roles = new HashSet<>(user.getRoles());
                roles.add(tenantAdmin);
                user.setRoles(roles);
                userRepository.save(user);
            });
        }

        tenantSchemaProvisioningService.provisionSchema(tenantId);

        return TenantResponse.from(findOrThrow(tenantId));
    }

    @Override
    @Transactional
    public TenantResponse reject(String tenantId, String reason) {
        TenantEntity tenant = findOrThrow(tenantId);
        if (!Constants.STATUS_PENDING.equals(tenant.getStatus())) {
            throw new AppException("TENANT_NOT_PENDING", "Only a pending tenant registration can be rejected");
        }
        tenant.setStatus(Constants.STATUS_DELETE);
        tenant.setRejectionReason(reason);
        tenant.setUpdatedAt(new Date());
        tenantRepository.save(tenant);

        subscriptionService.rejectPending(tenantId);

        return TenantResponse.from(tenant);
    }

    private TenantEntity findOrThrow(String id) {
        return tenantRepository.findById(id)
            .orElseThrow(() -> new AppException("TENANT_NOT_FOUND", "Tenant not found: " + id));
    }
}
