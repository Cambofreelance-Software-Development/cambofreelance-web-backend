package com.cambofreelance.authenticationservice.dto;

import com.cambofreelance.authenticationservice.constants.Constants;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -8892838641805537110L;
  private LocalDateTime createdAt = LocalDateTime.now();
  private String createdBy = Constants.SYSTEM;
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime updatedAt;
  private String updatedBy;
  private String status = Constants.STATUS_ACTIVE;

}
