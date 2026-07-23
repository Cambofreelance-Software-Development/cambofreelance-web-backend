package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.HardwareRequest;
import com.cambofreelance.webbackend.dto.response.HardwareResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface HardwareService {

    List<HardwareResponse> listAll();

    List<HardwareResponse> listPublic();

    Page<HardwareResponse> search(String search, int page, int size);

    HardwareResponse getById(String id);

    HardwareResponse create(HardwareRequest request, String createdBy);

    HardwareResponse update(String id, HardwareRequest request, String updatedBy);

    void delete(String id);
}
