package cambo.freelance.webservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "contact_info")
public class ContactInfo {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String address;

    @Column(name = "office_hours")
    private String officeHours;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}