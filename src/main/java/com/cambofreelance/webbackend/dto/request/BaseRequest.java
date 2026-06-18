package com.cambofreelance.webbackend.dto.request;

import com.cambofreelance.webbackend.dto.taxonomy.request.FilterRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.PaginationRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest {
    private String userId;
    private String username;
    private String phoneNumber;
    private String email;
    private String applicationType;
    private String status;
    private String password;
    private String confirmPassword;
    private String deviceId;
    private String osType;
    private String osVersion;
    private String ip;
    private String lang;
    private String clientSecret;
    private String clientId;
    private String sessionId;
    private String application;
    private List<FilterRequest> filter;
    private PaginationRequest paginate = new PaginationRequest();
    private String sortBy;
    private String sortDirection;
    private String search;
    private String applicationFeatureId;
    private String applicationFeatureDetailId;
    private String code;
    private String requesterUsername;
}
