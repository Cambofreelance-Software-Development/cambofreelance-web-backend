package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FaqRequest;
import com.cambofreelance.webbackend.dto.response.FaqResponse;
import com.cambofreelance.webbackend.entities.FaqEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.FaqRepository;
import com.cambofreelance.webbackend.services.FaqService;
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
public class FaqServiceImpl implements FaqService {

    private final FaqRepository repository;

    @Override
    public List<FaqResponse> listAll() {
        return repository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(FaqResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<FaqResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return repository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(FaqResponse::from);
    }

    @Override
    public FaqResponse getById(String id) {
        return FaqResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "FAQ")
    public FaqResponse create(FaqRequest request, String createdBy) {
        FaqEntity entity = new FaqEntity();
        entity.setId(UUID.randomUUID().toString());
        apply(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return FaqResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "FAQ", entityClass = FaqEntity.class)
    public FaqResponse update(String id, FaqRequest request, String updatedBy) {
        FaqEntity entity = requireById(id);
        apply(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return FaqResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "FAQ", entityClass = FaqEntity.class)
    public void delete(String id) {
        FaqEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        repository.save(entity);
    }

    private void apply(FaqEntity entity, FaqRequest request) {
        entity.setQuestion(request.getQuestion().trim());
        entity.setQuestionKh(request.getQuestionKh());
        entity.setAnswer(request.getAnswer().trim());
        entity.setAnswerKh(request.getAnswerKh());
    }

    private FaqEntity requireById(String id) {
        return repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("FAQ_NOT_FOUND", "FAQ not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
