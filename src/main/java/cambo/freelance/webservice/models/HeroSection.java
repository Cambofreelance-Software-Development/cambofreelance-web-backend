package cambo.freelance.webservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "hero_sections")
public class HeroSection {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String subtitle;
    private String description;

    @Column(name = "cta_text")
    private String ctaText;

    @Column(name = "cta_link")
    private String ctaLink;

    @Column(name = "background_image")
    private String backgroundImage;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}