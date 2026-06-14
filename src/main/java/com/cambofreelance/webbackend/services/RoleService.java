package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.RoleCloneRequest;
import com.cambofreelance.webbackend.dto.request.RoleRequest;
import com.cambofreelance.webbackend.dto.response.PermissionResponse;
import com.cambofreelance.webbackend.dto.response.RoleResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface RoleService {

    Page<RoleResponse> list(String search, String status, Integer level, int page, int size);

    RoleResponse getById(String id);

    RoleResponse create(RoleRequest request);

    RoleResponse update(String id, RoleRequest request);

    void delete(String id);

    RoleResponse toggleStatus(String id, String newStatus);

    RoleResponse clone(String id, RoleCloneRequest request);

    RoleResponse assignPermissions(String id, List<String> permissionIds);

    List<PermissionResponse> listPermissions();
}
