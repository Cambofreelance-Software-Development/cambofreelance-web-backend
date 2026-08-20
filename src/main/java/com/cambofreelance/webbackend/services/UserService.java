package com.cambofreelance.webbackend.services;


import com.cambofreelance.webbackend.dto.request.AdminUserCreateRequest;
import com.cambofreelance.webbackend.dto.request.AdminUserUpdateRequest;
import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.ChangePasswordRequest;
import com.cambofreelance.webbackend.dto.request.OAuthRequest;
import com.cambofreelance.webbackend.dto.request.UpdateProfileRequest;
import com.cambofreelance.webbackend.dto.request.UserCreateRequest;
import com.cambofreelance.webbackend.dto.request.ForgotPasswordRequest;
import com.cambofreelance.webbackend.dto.request.ResetPasswordRequest;
import com.cambofreelance.webbackend.dto.request.UserRegisterRequest;
import com.cambofreelance.webbackend.dto.response.RoleResponse;
import com.cambofreelance.webbackend.dto.response.UserListResponse;
import com.cambofreelance.webbackend.dto.response.UserProfileResponse;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
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

    UserProfileResponse adminUpdateUserApproval(String userId, String approvalStatus, String adminId) throws AppException;

    Set<String> getPermissionCodes(String userId);

    UserProfileResponse toProfileResponse(UserEntity user);

    /** Generates a 6-digit OTP, stores it in Redis, and emails it to the account's address. */
    void forgotPassword(ForgotPasswordRequest request) throws AppException;

    void resetPassword(ResetPasswordRequest request) throws AppException;
}
