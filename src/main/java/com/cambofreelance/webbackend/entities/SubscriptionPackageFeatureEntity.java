package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "subscription_package_features")
@Data
@DynamicUpdate
public class SubscriptionPackageFeatureEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private SubscriptionPackageFeatureId id;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public SubscriptionPackageFeatureEntity() {
    }

    public SubscriptionPackageFeatureEntity(String packageId, String featureCode, boolean enabled) {
        this.id = new SubscriptionPackageFeatureId(packageId, featureCode);
        this.enabled = enabled;
    }
}
