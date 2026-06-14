package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.response.AuditLogResponse;
import com.cambofreelance.webbackend.entities.AuditLogEntity;
import com.cambofreelance.webbackend.repository.AuditLogRepository;
import com.cambofreelance.webbackend.services.AuditLogService;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(String userId, String username, String action, String module,
                    String entityId, String description,
                    String oldValue, String newValue,
                    String ipAddress, String userAgent) {
        try {
            AuditLogEntity entry = new AuditLogEntity();
            entry.setId(UUID.randomUUID().toString());
            entry.setUserId(userId);
            entry.setUsername(username);
            entry.setAction(action);
            entry.setModule(module);
            entry.setEntityId(entityId);
            entry.setDescription(description);
            entry.setOldValue(oldValue);
            entry.setNewValue(newValue);
            entry.setIpAddress(ipAddress);
            entry.setUserAgent(userAgent);
            entry.setCreatedAt(new Date());
            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to write audit log [{} {}]: {}", action, module, e.getMessage());
        }
    }

    @Override
    public Page<AuditLogResponse> list(String userId, String action, String module,
                                       String search, String dateFrom, String dateTo,
                                       int page, int size) {
        Specification<AuditLogEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(userId)) {
                predicates.add(cb.equal(root.get("userId"), userId.trim()));
            }
            if (StringUtils.hasText(action)) {
                predicates.add(cb.equal(root.get("action"), action.trim().toUpperCase()));
            }
            if (StringUtils.hasText(module)) {
                predicates.add(cb.equal(root.get("module"), module.trim().toUpperCase()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("username")), like),
                    cb.like(cb.lower(root.get("description")), like),
                    cb.like(cb.lower(root.get("entityId")), like)
                ));
            }
            if (StringUtils.hasText(dateFrom)) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), parseDate(dateFrom)));
            }
            if (StringUtils.hasText(dateTo)) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), parseEndOfDay(dateTo)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return auditLogRepository.findAll(spec, pageable).map(AuditLogResponse::from);
    }

    @Override
    @Transactional
    public int purge(int retentionDays) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -retentionDays);
        int deleted = auditLogRepository.deleteByCreatedAtBefore(cal.getTime());
        log.info("Purged {} audit log entries older than {} days", deleted, retentionDays);
        return deleted;
    }

    private Date parseDate(String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(value);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    private Date parseEndOfDay(String value) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(value));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return cal.getTime();
        } catch (ParseException e) {
            return new Date();
        }
    }
}
