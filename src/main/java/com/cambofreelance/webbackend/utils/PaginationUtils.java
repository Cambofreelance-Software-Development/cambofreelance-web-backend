package com.cambofreelance.webbackend.utils;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.PaginationRequest;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginationMetadata;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private PaginationUtils() {
    }

    public static PaginationMetadata from(Page<?> page) {
        var metadata = new PaginationMetadata();
        metadata.setPage(page.getNumber() + 1);
        metadata.setSize(page.getSize());
        metadata.setTotalElements(page.getTotalElements());
        metadata.setTotalPages(page.getTotalPages());
        metadata.setFirst(page.isFirst());
        metadata.setLast(page.isLast());
        metadata.setHasPrevious(page.hasPrevious());
        metadata.setHasNext(page.hasNext());
        return metadata;
    }

    public static Pageable toPageable(BaseRequest req, String defaultSortBy) {
        String sortBy = Optional.ofNullable(req.getSortBy()).filter(s -> !s.isBlank()).orElse(defaultSortBy);
        String sortDir = Optional.ofNullable(req.getSortDirection()).filter(s -> !s.isBlank()).orElse("desc");
        Sort.Direction direction = Sort.Direction.fromString(sortDir);

        int page = Optional.ofNullable(req.getPaginate()).map(p -> p.getPage() - 1).orElse(0);
        int size = Optional.ofNullable(req.getPaginate()).map(PaginationRequest::getSize).orElse(20);

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
