package com.cambofreelance.authenticationservice.services;


import com.cambofreelance.authenticationservice.dto.request.AdminUserCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.AdminUserUpdateRequest;
import com.cambofreelance.authenticationservice.dto.request.BaseRequest;
import com.cambofreelance.authenticationservice.dto.request.ChangePasswordRequest;
import com.cambofreelance.authenticationservice.dto.request.OAuthRequest;
import com.cambofreelance.authenticationservice.dto.request.UpdateProfileRequest;
import com.cambofreelance.authenticationservice.dto.request.UserCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.ForgotPasswordRequest;
import com.cambofreelance.authenticationservice.dto.request.ResetPasswordRequest;
import com.cambofreelance.authenticationservice.dto.request.UserRegisterRequest;
import com.cambofreelance.authenticationservice.dto.response.RoleResponse;
import com.cambofreelance.authenticationservice.dto.response.UserListResponse;
import com.cambofreelance.authenticationservice.dto.response.UserProfileResponse;
import com.cambofreelance.authenticationservice.entities.UserEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import java.util.List;
import java.util.Set;

public interface UserService {

    UserEntity authUser(OAuthRequest authRequest) throws AppException;

    UserEntity checkUser(BaseRequest request) throws AppException;

    UserEntity createUser(UserCreateRequest request) throws AppException;

    UserEntity updateUser(UserCreateRequest request) throws AppException;

    UserEntity registerUser(UserRegisterRequest req) throws AppException;

    UserEntity getUserById(String userId) throws AppException;

    UserProfileResponse getUserProfile(String userId) throws AppException;

    void changePassword(String userId, ChangePasswordRequest request) throws AppException;

    UserProfileResponse updateProfile(String userId, UpdateProfileRequest request) throws AppException;

    UserListResponse getUserList(String search, String status, String roleId, int page, int size) throws AppException;

    List<RoleResponse> getAllRoles() throws AppException;

    UserProfileResponse adminCreateUser(AdminUserCreateRequest request) throws AppException;

    UserProfileResponse adminUpdateUser(String userId, AdminUserUpdateRequest request) throws AppException;

    void adminDeleteUser(String userId) throws AppException;

    UserProfileResponse adminUpdateUserStatus(String userId, String status) throws AppException;

    Set<String> getPermissionCodes(String userId);

    /** Generates a 6-digit OTP stored in Redis and returns it (dev mode — no email). */
    String forgotPassword(ForgotPasswordRequest request) throws AppException;

    void resetPassword(ResetPasswordRequest request) throws AppException;

    UserEntity findOrCreateSocialUser(SocialAuthService.SocialUserInfo info) throws AppException;
}
