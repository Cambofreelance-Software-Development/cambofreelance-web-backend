package cambo.freelance.webservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "contact_messages")
public class ContactMessage {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String subject;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    private String status = "unread";

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}