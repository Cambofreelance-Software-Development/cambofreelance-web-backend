package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("userType")
    private String userType;

    @JsonProperty("status")
    private String status;

    @JsonProperty("approvalStatus")
    private String approvalStatus;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;

    @JsonProperty("roles")
    private List<RoleItem> roles;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleItem {
        @JsonProperty("roleId")
        private String roleId;

        @JsonProperty("roleName")
        private String roleName;

        @JsonProperty("roleCode")
        private String roleCode;
    }
}
