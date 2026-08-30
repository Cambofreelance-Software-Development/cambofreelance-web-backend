package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.constants.RegisterChannel;
import com.cambofreelance.webbackend.dto.request.AdminUserCreateRequest;
import com.cambofreelance.webbackend.dto.request.AdminUserUpdateRequest;
import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.ChangePasswordRequest;
import com.cambofreelance.webbackend.dto.request.OAuthRequest;
import com.cambofreelance.webbackend.dto.request.UpdateProfileRequest;
import com.cambofreelance.webbackend.dto.request.UserCreateRequest;
import com.cambofreelance.webbackend.caches.PasswordResetCache;
import com.cambofreelance.webbackend.caches.RegisterOtpCache;
import com.cambofreelance.webbackend.constants.OtpChannel;
import com.cambofreelance.webbackend.services.SmsService;
import com.cambofreelance.webbackend.dto.request.ForgotPasswordRequest;
import com.cambofreelance.webbackend.dto.request.ResetPasswordRequest;
import com.cambofreelance.webbackend.dto.request.UserRegisterRequest;
import com.cambofreelance.webbackend.dto.response.RoleResponse;
import com.cambofreelance.webbackend.dto.response.UserListResponse;
import com.cambofreelance.webbackend.dto.response.UserProfileResponse;
import com.cambofreelance.webbackend.entities.RoleEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.RoleRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.EmailService;
import com.cambofreelance.webbackend.services.RefreshTokenService;
import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.services.UserService;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PasswordResetCache passwordResetCache;
    private final RegisterOtpCache registerOtpCache;
    private final EmailService emailService;
    private final SmsService smsService;

    private static final SecureRandom RANDOM = new SecureRandom();

    /** Excludes visually-ambiguous characters (0/O, 1/I) — this code gets typed in by hand. */
    private static final String REFERRAL_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int REFERRAL_CODE_LENGTH = 8;

    @Override
    public UserEntity authUser(OAuthRequest authRequest) throws AppException {
        UserEntity username = userRepository.findByUsernameAndStatus(
                authRequest.getUsername(),  Constants.STATUS_ACTIVE)
            .orElse(null);
        if (username == null) {
            username = userRepository.findByPhoneNumberAndStatus(
                    authRequest.getUsername(),
                    Constants.STATUS_ACTIVE)
                .orElse(null);
        }
        if (username == null) {
            username = userRepository.findByEmailAndStatus(
                authRequest.getUsername(),Constants.STATUS_ACTIVE).orElse(null);
        }
        return username;
    }

    @Override
    public UserEntity checkUser(BaseRequest request) throws AppException {
        if (request.getApplicationType() == null) {
            return userRepository.findByUsernameAndStatus(request.getUsername(),
                request.getStatus()).orElse(null);
        } else if (request.getUserId() != null) {
            return userRepository.findByUserIdAndStatus(request.getUserId(), request.getStatus())
                .orElse(null);
        }
        return userRepository.findByUsernameAndStatus(request.getUsername(),request.getStatus()).orElse(null);
    }

    @Override
    public UserEntity createUser(UserCreateRequest request) throws AppException {
        var checkUserName = userRepository.findByUsernameAndStatus(
            request.getUsername(), Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "");
        }

        var checkPhoneNumber = userRepository.findByPhoneNumberAndStatus(
            request.getPhoneNumber(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent()) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "");
        }

        var checkEmail = userRepository.findByEmailAndStatus(
            request.getEmail(),  Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "");
        }

        UserEntity userEntity = new UserEntity();
        if (request.getStatus() == null) {
            request.setStatus(Constants.STATUS_ACTIVE);
        }
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(Strings.isBlank(request.getPassword()) ? Constants.PASSWORD : bCryptPasswordEncoder.encode(request.getPassword()));
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setApplicationId(request.getApplicationType());
        userEntity.setUserType(request.getUserType());
        userEntity.setReferralCode(generateReferralCode());
        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UserCreateRequest request) throws AppException {
        var checkUser = userRepository.findByUserIdAndStatus(request.getUserId(),
            request.getStatus());
        if (checkUser.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "");
        }

        var checkUserName = userRepository.findByUsernameAndStatus(
            request.getUsername(), Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent() && !checkUser.get().getUserId()
            .equals(checkUserName.get().getUserId())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "");
        }

        var checkPhoneNumber = userRepository.findByPhoneNumberAndStatus(
            request.getPhoneNumber(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent() && !checkUser.get().getUserId()
            .equals(checkPhoneNumber.get().getUserId())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "Phone is already exist");
        }

        var checkEmail = userRepository.findByEmailAndStatus(
            request.getEmail(), Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent() && !checkUser.get().getUserId()
            .equals(checkEmail.get().getUserId())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "");
        }

        UserEntity userEntity = checkUser.get();
        if (request.getStatus() == null) {
            request.setStatus(Constants.STATUS_ACTIVE);
        }
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        if (request.getPassword() != null) {
            userEntity.setPassword(request.getPassword());
        }
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setApplicationId(request.getApplicationType());
        userEntity.setUserType(request.getUserType());
        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity registerUser(UserRegisterRequest request) throws AppException {
        var checkUserName = userRepository.findByUsernameAndStatus(request.getUsername(), Constants.STATUS_ACTIVE);
        if (checkUserName.isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "");
        }

        var checkPhoneNumber = userRepository.findByPhoneNumberAndStatus(
            request.getPhoneNumber(), Constants.STATUS_ACTIVE);
        if (checkPhoneNumber.isPresent()) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "");
        }

        var checkEmail = userRepository.findByEmailAndStatus(
            request.getEmail(),  Constants.STATUS_ACTIVE);
        if (checkEmail.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(Constants.STATUS_ACTIVE);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(Strings.isBlank(request.getPassword()) ? Constants.PASSWORD : bCryptPasswordEncoder.encode(request.getPassword()));
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setUserType(Constants.USER);
        // Self-registered accounts must be approved by an admin before they can subscribe
        userEntity.setApprovalStatus(Constants.APPROVAL_PENDING);
        // Must confirm a register OTP (phone or email — user's choice) before this account can
        // log in — see OAuthAuthenticator.
        userEntity.setPhoneVerified(false);
        userEntity.setEmailVerified(false);

        userEntity.setReferralCode(generateReferralCode());
        if (StringUtils.hasText(request.getReferralCode())) {
            UserEntity referrer = userRepository
                .findByReferralCodeAndStatus(request.getReferralCode().trim().toUpperCase(), Constants.STATUS_ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFERRAL_CODE, "Invalid referral code"));
            userEntity.setReferredBy(referrer.getUserId());
        }

        // Assign PUBLIC_USER role automatically on self-registration
        roleRepository.findByCode("PUBLIC_USER").ifPresent(role -> {
            userEntity.getRoles().add(role);
        });

        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public void sendRegisterOtp(String userId, String channel) throws AppException {
        validateOtpChannel(channel);
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found"));

        if (registerOtpCache.isOnCooldown(channel, userId)) {
            throw new AppException(ErrorCode.OTP_SEND_LIMIT_EXCEEDED, "Please wait before requesting another code.");
        }
        if (registerOtpCache.isDailySendLimitExceeded(channel, userId)) {
            throw new AppException(ErrorCode.OTP_SEND_LIMIT_EXCEEDED, "Daily OTP send limit reached. Please try again tomorrow.");
        }
        registerOtpCache.markSent(channel, userId);
        registerOtpCache.incrementDailySendCount(channel, userId);

        if (OtpChannel.PHONE.equals(channel)) {
            // Twilio Verify generates and holds the code itself — nothing to cache locally.
            smsService.sendVerification(user.getPhoneNumber());
        } else {
            String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
            registerOtpCache.store(channel, userId, otp);
            emailService.sendVerificationOtp(user.getEmail(), otp);
        }
    }

    @Override
    @Transactional
    public void verifyRegisterOtp(String userId, String channel, String otp) throws AppException {
        validateOtpChannel(channel);
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found"));

        if (OtpChannel.PHONE.equals(channel)) {
            if (!smsService.checkVerification(user.getPhoneNumber(), otp)) {
                throw new AppException(ErrorCode.INVALID_OTP, "Invalid verification code.");
            }
            user.setPhoneVerified(true);
        } else {
            String stored = registerOtpCache.get(channel, userId);
            if (stored == null) {
                throw new AppException(ErrorCode.OTP_EXPIRED, "Verification code has expired. Please request a new one.");
            }
            if (!stored.equals(otp)) {
                throw new AppException(ErrorCode.INVALID_OTP, "Invalid verification code.");
            }
            user.setEmailVerified(true);
            registerOtpCache.delete(channel, userId);
        }
        // A confirmed phone or email is enough proof of a real account — skip the admin
        // approval queue, same as Google-registered accounts get on sign-up.
        user.setApprovalStatus(Constants.APPROVAL_APPROVED);
        userRepository.save(user);
    }

    private void validateOtpChannel(String channel) throws AppException {
        if (!OtpChannel.PHONE.equals(channel) && !OtpChannel.EMAIL.equals(channel)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "channel must be PHONE or EMAIL");
        }
    }

    @Override
    public UserEntity resolveAccountForVerification(String username, String password) throws AppException {
        OAuthRequest lookup = new OAuthRequest();
        lookup.setUsername(username);
        UserEntity user = authUser(lookup);

        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }
        return user;
    }

    @Override
    @Transactional
    public UserEntity findOrCreateGoogleUser(String email) throws AppException {
        Optional<UserEntity> existing = userRepository.findByEmailAndStatus(email, Constants.STATUS_ACTIVE);
        if (existing.isPresent()) {
            return existing.get();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(Constants.STATUS_ACTIVE);
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setEmail(email);
        userEntity.setUsername(generateUniqueUsername(email));
        // Never disclosed/returned — Google-provisioned accounts can't log in via grant_type=password.
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        userEntity.setPassword(bCryptPasswordEncoder.encode(Base64.getEncoder().encodeToString(randomBytes)));
        userEntity.setUserType(Constants.USER);
        userEntity.setRegisterChannel(RegisterChannel.GOOGLE);
        userEntity.setReferralCode(generateReferralCode());
        // Google verifies the email, so unlike self-registration these accounts don't need admin approval.
        userEntity.setApprovalStatus(Constants.APPROVAL_APPROVED);
        // Google verifies the email, and there's no phone number to verify on this path anyway.
        userEntity.setPhoneVerified(true);
        userEntity.setEmailVerified(true);

        roleRepository.findByCode("PUBLIC_USER").ifPresent(role -> userEntity.getRoles().add(role));

        userRepository.save(userEntity);
        return userEntity;
    }

    private String generateUniqueUsername(String email) {
        String localPart = email.substring(0, email.indexOf('@')).toLowerCase()
            .replaceAll("[^a-z0-9._-]", "");
        if (localPart.isBlank()) {
            localPart = "user";
        }
        String candidate = localPart;
        while (userRepository.findByUsernameAndStatus(candidate, Constants.STATUS_ACTIVE).isPresent()) {
            candidate = localPart + RANDOM.nextInt(10000, 100000);
        }
        return candidate;
    }

    private String generateReferralCode() {
        String candidate;
        do {
            StringBuilder sb = new StringBuilder(REFERRAL_CODE_LENGTH);
            for (int i = 0; i < REFERRAL_CODE_LENGTH; i++) {
                sb.append(REFERRAL_CODE_CHARS.charAt(RANDOM.nextInt(REFERRAL_CODE_CHARS.length())));
            }
            candidate = sb.toString();
        } while (userRepository.existsByReferralCode(candidate));
        return candidate;
    }

    @Override
    public UserEntity getUserById(String userId) throws AppException {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    @Transactional
    public UserProfileResponse getUserProfile(String userId) throws AppException {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        List<UserProfileResponse.RoleItem> roles = user.getRoles().stream()
            .map(r -> UserProfileResponse.RoleItem.builder()
                .roleId(r.getId())
                .roleName(r.getName())
                .roleCode(r.getCode())
                .build())
            .collect(Collectors.toList());

        return UserProfileResponse.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .userType(user.getUserType())
            .status(user.getStatus())
            .approvalStatus(user.getApprovalStatus())
            .referralCode(user.getReferralCode())
            .createdAt(user.getCreatedAt())
            .roles(roles)
            .build();
    }

    @Override
    @Auditable(action = "PASSWORD_CHANGE", module = "USER")
    public void changePassword(String userId, ChangePasswordRequest request) throws AppException {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.CONFIRM_CURRENT_PASSWORD_NOT_MATCH, "Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH, "Passwords do not match");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserListResponse getUserList(String search, String status, String roleId, int page, int size) throws AppException {
        Specification<UserEntity> spec = buildUserSpec(search, status, roleId);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserEntity> userPage = userRepository.findAll(spec, pageable);

        List<UserProfileResponse> content = userPage.getContent().stream()
            .map(this::toProfileResponse)
            .collect(Collectors.toList());

        return UserListResponse.builder()
            .content(content)
            .totalElements(userPage.getTotalElements())
            .totalPages(userPage.getTotalPages())
            .currentPage(page)
            .pageSize(size)
            .first(userPage.isFirst())
            .last(userPage.isLast())
            .build();
    }

    @Override
    public List<RoleResponse> getAllRoles() throws AppException {
        return roleRepository.findAllByStatus(Constants.STATUS_ACTIVE).stream()
            .map(r -> RoleResponse.builder()
                .roleId(r.getId())
                .roleName(r.getName())
                .code(r.getCode())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public UserProfileResponse toProfileResponse(UserEntity user) {
        List<UserProfileResponse.RoleItem> roles = user.getRoles().stream()
            .map(r -> UserProfileResponse.RoleItem.builder()
                .roleId(r.getId())
                .roleName(r.getName())
                .roleCode(r.getCode())
                .build())
            .collect(Collectors.toList());

        return UserProfileResponse.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .userType(user.getUserType())
            .status(user.getStatus())
            .approvalStatus(user.getApprovalStatus())
            .referralCode(user.getReferralCode())
            .createdAt(user.getCreatedAt())
            .roles(roles)
            .build();
    }

    private Specification<UserEntity> buildUserSpec(String search, String status, String roleId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                    cb.like(root.get("phoneNumber"), "%" + search + "%")
                ));
            }

            if (StringUtils.hasText(status)) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (StringUtils.hasText(roleId)) {
                // subquery to avoid duplicate rows from ManyToMany join
                Subquery<String> sub = query.subquery(String.class);
                Root<UserEntity> subRoot = sub.from(UserEntity.class);
                sub.select(subRoot.get("userId"));
                sub.where(cb.equal(subRoot.join("roles", JoinType.INNER).get("id"), roleId));
                predicates.add(root.get("userId").in(sub));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(String userId, UpdateProfileRequest request) throws AppException {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "Email already in use"); });
            user.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "Phone number already in use"); });
            user.setPhoneNumber(request.getPhoneNumber());
        }

        userRepository.save(user);
        return getUserProfile(userId);
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "USER")
    public UserProfileResponse adminCreateUser(AdminUserCreateRequest request) throws AppException {
        if (userRepository.findByUsernameAndStatus(request.getUsername(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "Username already exists");
        }
        if (StringUtils.hasText(request.getEmail()) &&
            userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "Email already in use");
        }
        if (StringUtils.hasText(request.getPhoneNumber()) &&
            userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), Constants.STATUS_ACTIVE).isPresent()) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "Phone number already in use");
        }

        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(
            StringUtils.hasText(request.getPassword()) ? request.getPassword() : Constants.PASSWORD
        ));
        user.setUserType(StringUtils.hasText(request.getUserType()) ? request.getUserType() : Constants.USER);
        user.setStatus(Constants.STATUS_ACTIVE);
        user.setReferralCode(generateReferralCode());
        // Admin-created accounts are trusted, no separate approval step needed
        user.setApprovalStatus(Constants.APPROVAL_APPROVED);
        // Admin-created, so there's no self-service OTP step to gate on
        user.setPhoneVerified(true);
        user.setEmailVerified(true);

        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<RoleEntity> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            user.setRoles(roles);
        }

        userRepository.save(user);
        return toProfileResponse(user);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "USER", entityClass = UserEntity.class)
    public UserProfileResponse adminUpdateUser(String userId, AdminUserUpdateRequest request) throws AppException {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));

        if (StringUtils.hasText(request.getUsername()) && !request.getUsername().equals(user.getUsername())) {
            userRepository.findByUsernameAndStatus(request.getUsername(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException(ErrorCode.USERNAME_ALREADY_EXIST, "Username already taken"); });
            user.setUsername(request.getUsername());
        }
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(user.getEmail())) {
            userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST, "Email already in use"); });
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPhoneNumber()) && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            userRepository.findByPhoneNumberAndStatus(request.getPhoneNumber(), Constants.STATUS_ACTIVE)
                .ifPresent(u -> { throw new AppException(ErrorCode.PHONE_ALREADY_EXIST, "Phone already in use"); });
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (StringUtils.hasText(request.getUserType())) {
            user.setUserType(request.getUserType());
        }
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }
        if (request.getRoleIds() != null) {
            Set<RoleEntity> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            user.setRoles(roles);
        }

        userRepository.save(user);
        return toProfileResponse(user);
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "USER", entityClass = UserEntity.class)
    public void adminDeleteUser(String userId) throws AppException {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));
        user.setStatus(Constants.STATUS_DELETE);
        userRepository.save(user);
        refreshTokenService.revokeAllByUserId(userId);
    }

    @Override
    @Transactional
    @Auditable(action = "STATUS_CHANGE", module = "USER", entityClass = UserEntity.class)
    public UserProfileResponse adminUpdateUserStatus(String userId, String status) throws AppException {
        if (!List.of(Constants.STATUS_ACTIVE, Constants.STATUS_LEAVE, Constants.STATUS_DELETE).contains(status)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Invalid status value");
        }
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));
        user.setStatus(status);
        userRepository.save(user);
        // invalidate all sessions whenever account is deactivated or deleted
        if (!Constants.STATUS_ACTIVE.equals(status)) {
            refreshTokenService.revokeAllByUserId(userId);
        }
        return toProfileResponse(user);
    }

    @Override
    @Transactional
    @Auditable(action = "APPROVAL_CHANGE", module = "USER", entityClass = UserEntity.class)
    public UserProfileResponse adminUpdateUserApproval(String userId, String approvalStatus, String adminId) throws AppException {
        if (!List.of(Constants.APPROVAL_APPROVED, Constants.APPROVAL_REJECTED, Constants.APPROVAL_PENDING).contains(approvalStatus)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Invalid approval status value");
        }
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "User not found"));
        user.setApprovalStatus(approvalStatus);
        user.setApprovedBy(adminId);
        user.setApprovedAt(new Date());
        userRepository.save(user);
        return toProfileResponse(user);
    }

    @Override
    public Set<String> getPermissionCodes(String userId) {
        return userRepository.findActivePermissionCodesByUserId(userId);
    }

    // ── Password reset ────────────────────────────────────────────────────────

    @Override
    public void forgotPassword(ForgotPasswordRequest request) throws AppException {
        // Always respond the same way to prevent email enumeration.
        // If the email doesn't exist we still return success, but don't store or send an OTP.
        Optional<UserEntity> userOpt = userRepository.findByEmailAndStatus(
            request.getEmail(), Constants.STATUS_ACTIVE);

        if (userOpt.isEmpty()) {
            return;
        }

        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        passwordResetCache.store(request.getEmail(), otp);
        emailService.sendPasswordResetOtp(request.getEmail(), otp);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) throws AppException {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH, "Passwords do not match");
        }

        String stored = passwordResetCache.get(request.getEmail());
        if (stored == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED, "Reset code has expired. Please request a new one.");
        }
        if (!stored.equals(request.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP, "Invalid reset code.");
        }

        UserEntity user = userRepository.findByEmailAndStatus(request.getEmail(), Constants.STATUS_ACTIVE)
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found"));

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        passwordResetCache.delete(request.getEmail());
    }
}
