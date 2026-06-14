package com.cambofreelance.authenticationservice.entities;

import com.cambofreelance.authenticationservice.constants.Constants;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -8892838641805537110L;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @Column(name = "CREATED_BY")
    private String createdBy = Constants.SYSTEM;
    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "STATUS")
    private String status = Constants.STATUS_ACTIVE;

}
