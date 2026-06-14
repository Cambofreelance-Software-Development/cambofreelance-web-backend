package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.RoleCloneRequest;
import com.cambofreelance.webbackend.dto.request.RoleRequest;
import com.cambofreelance.webbackend.dto.response.PermissionResponse;
import com.cambofreelance.webbackend.dto.response.RoleResponse;
import com.cambofreelance.webbackend.entities.PermissionEntity;
import com.cambofreelance.webbackend.entities.RoleEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.PermissionRepository;
import com.cambofreelance.webbackend.repository.RoleRepository;
import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.services.RoleService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Page<RoleResponse> list(String search, String status, Integer level, int page, int size) {
        Specification<RoleEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("status"), Constants.STATUS_DELETE));
            if (search != null && !search.isBlank()) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("code")), like)
                ));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status.trim()));
            }
            if (level != null) {
                predicates.add(cb.equal(root.get("level"), level));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.asc("level").nullsLast()).and(Sort.by("name").ascending());
        return roleRepository.findAll(spec, PageRequest.of(page, size, sort)).map(RoleResponse::from);
    }

    @Override
    public RoleResponse getById(String id) {
        return RoleResponse.from(findActive(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "ROLE")
    public RoleResponse create(RoleRequest request) {
        String code = request.getCode().trim().toUpperCase();
        if (roleRepository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            conflict("Role code '" + code + "' already exists");
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(code);
        entity.setName(request.getName().trim());
        entity.setConvention(request.getConvention());
        entity.setLevel(request.getLevel());
        entity.setDescription(request.getDescription());
        entity.setCreatedBy(Constants.SYSTEM);
        entity.setPermissions(resolvePermissions(request.getPermissionIds()));

        return RoleResponse.from(roleRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "ROLE", entityClass = RoleEntity.class)
    public RoleResponse update(String id, RoleRequest request) {
        RoleEntity entity = findActive(id);
        String code = request.getCode().trim().toUpperCase();

        if (!code.equals(entity.getCode())
                && roleRepository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            conflict("Role code '" + code + "' already exists");
        }

        entity.setCode(code);
        entity.setName(request.getName().trim());
        entity.setConvention(request.getConvention());
        entity.setLevel(request.getLevel());
        entity.setDescription(request.getDescription());
        entity.setUpdatedAt(new Date());
        entity.setPermissions(resolvePermissions(request.getPermissionIds()));

        return RoleResponse.from(roleRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "ROLE", entityClass = RoleEntity.class)
    public void delete(String id) {
        RoleEntity entity = findActive(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        roleRepository.save(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "STATUS_CHANGE", module = "ROLE", entityClass = RoleEntity.class)
    public RoleResponse toggleStatus(String id, String newStatus) {
        RoleEntity entity = findNotDeleted(id);
        if (!Constants.STATUS_ACTIVE.equals(newStatus) && !"DEA".equals(newStatus)) {
            AppException ex = new AppException("INVALID_STATUS", "Status must be ACT or DEA");
            ex.setHttpStatus(HttpStatus.BAD_REQUEST);
            throw ex;
        }
        entity.setStatus(newStatus);
        entity.setUpdatedAt(new Date());
        return RoleResponse.from(roleRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "CLONE", module = "ROLE")
    public RoleResponse clone(String id, RoleCloneRequest request) {
        RoleEntity source = findNotDeleted(id);
        String code = request.getCode().trim().toUpperCase();
        if (roleRepository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            conflict("Role code '" + code + "' already exists");
        }

        RoleEntity copy = new RoleEntity();
        copy.setId(UUID.randomUUID().toString());
        copy.setCode(code);
        copy.setName(request.getName().trim());
        copy.setConvention(source.getConvention());
        copy.setLevel(source.getLevel());
        copy.setDescription(source.getDescription());
        copy.setCreatedBy(Constants.SYSTEM);
        copy.setPermissions(new HashSet<>(source.getPermissions()));

        return RoleResponse.from(roleRepository.save(copy));
    }

    @Override
    @Transactional
    @Auditable(action = "PERMISSION_ASSIGN", module = "ROLE")
    public RoleResponse assignPermissions(String id, List<String> permissionIds) {
        RoleEntity entity = findActive(id);
        entity.setPermissions(resolvePermissions(permissionIds));
        entity.setUpdatedAt(new Date());
        return RoleResponse.from(roleRepository.save(entity));
    }

    @Override
    public List<PermissionResponse> listPermissions() {
        return permissionRepository.findAllByOrderBySortOrderAsc()
            .stream()
            .map(PermissionResponse::from)
            .collect(Collectors.toList());
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private RoleEntity findActive(String id) {
        return roleRepository.findById(id)
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .orElseThrow(() -> notFound(id));
    }

    private RoleEntity findNotDeleted(String id) {
        return roleRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound(id));
    }

    private Set<PermissionEntity> resolvePermissions(List<String> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(permissionRepository.findAllById(ids));
    }

    private AppException notFound(String id) {
        AppException ex = new AppException("NOT_FOUND", "Role not found: " + id);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }

    private void conflict(String message) {
        AppException ex = new AppException("DUPLICATE_CODE", message);
        ex.setHttpStatus(HttpStatus.CONFLICT);
        throw ex;
    }
}
