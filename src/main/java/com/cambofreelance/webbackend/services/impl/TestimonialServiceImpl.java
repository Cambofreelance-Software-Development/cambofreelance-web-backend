package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.TestimonialRequest;
import com.cambofreelance.webbackend.dto.response.TestimonialResponse;
import com.cambofreelance.webbackend.entities.TestimonialEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.TestimonialRepository;
import com.cambofreelance.webbackend.services.TestimonialService;
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
public class TestimonialServiceImpl implements TestimonialService {

    private final TestimonialRepository repository;

    @Override
    public List<TestimonialResponse> listAll() {
        return repository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(TestimonialResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<TestimonialResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return repository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(TestimonialResponse::from);
    }

    @Override
    public TestimonialResponse getById(String id) {
        return TestimonialResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "TESTIMONIAL")
    public TestimonialResponse create(TestimonialRequest request, String createdBy) {
        TestimonialEntity entity = new TestimonialEntity();
        entity.setId(UUID.randomUUID().toString());
        apply(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return TestimonialResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "TESTIMONIAL", entityClass = TestimonialEntity.class)
    public TestimonialResponse update(String id, TestimonialRequest request, String updatedBy) {
        TestimonialEntity entity = requireById(id);
        apply(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return TestimonialResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "TESTIMONIAL", entityClass = TestimonialEntity.class)
    public void delete(String id) {
        TestimonialEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        repository.save(entity);
    }

    private void apply(TestimonialEntity entity, TestimonialRequest request) {
        entity.setQuote(request.getQuote().trim());
        entity.setQuoteKh(request.getQuoteKh());
        entity.setAuthorName(request.getAuthorName().trim());
        entity.setAuthorNameKh(request.getAuthorNameKh());
        entity.setLocation(request.getLocation());
        entity.setLocationKh(request.getLocationKh());
        entity.setAvatarUrl(request.getAvatarUrl());
    }

    private TestimonialEntity requireById(String id) {
        return repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("TESTIMONIAL_NOT_FOUND", "Testimonial not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
