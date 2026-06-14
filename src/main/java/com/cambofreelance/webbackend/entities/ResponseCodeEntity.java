package com.cambofreelance.webbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "RESPONSE_CODES")
@Getter
@Setter
@ToString
public class ResponseCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CODE", unique = true, nullable = false)
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HTTP_STATUS")
    private String httpStatus;

    // Reserved keyword → keep in brackets
    @Column(name = "[KEY]")
    private String key;

    @Column(name = "MESSAGE_EN")
    private String messageEn;

    @Column(name = "MESSAGE_CN")
    private String messageCn;

    @Column(name = "MESSAGE_KM")
    private String messageKm;

    // Reserved keyword → keep in brackets
    @Column(name = "[TYPE]")
    private String type;

    @Column(name = "SERVICE_TYPE", length = 100)
    private String serviceType;

    @Column(name = "DELETED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(name = "DELETED_BY", length = 100)
    private String deletedBy;

    @Column(name = "STATUS", length = 3)
    private String status = "ACT";
}
