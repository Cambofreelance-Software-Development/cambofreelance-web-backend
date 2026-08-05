package com.cambofreelance.webbackend.services;

import lombok.Builder;
import lombok.Data;

/** Result of a (possibly range-limited) object fetch from storage. */
@Data
@Builder
public class SpacesObject {

    private byte[] data;
    private long totalSize;
    private boolean partial;
    private Long rangeStart;
    private Long rangeEnd;
}
