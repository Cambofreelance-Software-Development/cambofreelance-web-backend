package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "USERS")
@Data
@DynamicUpdate()
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3846050144523519426L;
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "APPLICATION_ID")
    private String applicationId;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "REGISTER_CHANNEL")
    private String registerChannel;

    @Column(name = "PHONE_VERIFIED")
    private Boolean phoneVerified;

    @Column(name = "EMAIL_VERIFIED")
    private Boolean emailVerified;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Column(name = "APPROVED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @Column(name = "IS_FORCE_CHANGE_PASSWORD")
    private String isForceChangePassword;

    @Column(name = "PASSWORD_CHANGE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changePasswordAt;

    @Column(name = "INVALID_PASSWORD_COUNT")
    private Integer invalidPasswordCount;

    @Column(name = "INVALID_PASSWORD_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidPinAt;

    @Column(name = "INVALID_OTP_COUNT")
    private Integer invalidOtpCount;

    @Column(name = "INVALID_OTP_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invalidOtpAt;

    /** This user's own shareable code — other users enter it at registration to be attributed to them. */
    @Column(name = "referral_code")
    private String referralCode;

    /** userId of whoever referred this user in, resolved from the referral code entered at registration. */
    @Column(name = "referred_by")
    private String referredBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLES",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<RoleEntity> roles = new HashSet<>();

}
