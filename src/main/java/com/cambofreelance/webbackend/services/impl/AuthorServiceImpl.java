package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.AuthorRequest;
import com.cambofreelance.webbackend.dto.response.AuthorResponse;
import com.cambofreelance.webbackend.entities.AuthorEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.AuthorRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.services.AuthorService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final MediaRepository  mediaRepository;

    @Override
    public List<AuthorResponse> listAll() {
        return authorRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(AuthorResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return authorRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(AuthorResponse::from);
    }

    @Override
    public AuthorResponse getById(String id) {
        return AuthorResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "AUTHOR")
    public AuthorResponse create(AuthorRequest request, String createdBy) {
        AuthorEntity entity = new AuthorEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setBio(request.getBio());
        entity.setEmail(request.getEmail());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        resolveAvatar(entity, request.getAvatarId());
        return AuthorResponse.from(authorRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "AUTHOR", entityClass = AuthorEntity.class)
    public AuthorResponse update(String id, AuthorRequest request, String updatedBy) {
        AuthorEntity entity = requireById(id);
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setBio(request.getBio());
        entity.setEmail(request.getEmail());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        resolveAvatar(entity, request.getAvatarId());
        return AuthorResponse.from(authorRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "AUTHOR", entityClass = AuthorEntity.class)
    public void delete(String id) {
        AuthorEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        authorRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AuthorEntity requireById(String id) {
        return authorRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("AUTHOR_NOT_FOUND", "Author not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }

    private void resolveAvatar(AuthorEntity entity, String avatarId) {
        if (StringUtils.hasText(avatarId)) {
            MediaFileEntity avatar = mediaRepository.findById(avatarId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setAvatar(avatar);
        } else {
            entity.setAvatar(null);
        }
    }
}
