package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class SubscriptionPackageFeatureId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "package_id")
    private String packageId;

    @Column(name = "feature_code")
    private String featureCode;

    public SubscriptionPackageFeatureId(String packageId, String featureCode) {
        this.packageId = packageId;
        this.featureCode = featureCode;
    }
}
