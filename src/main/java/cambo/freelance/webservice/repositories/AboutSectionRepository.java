package cambo.freelance.webservice.repositories;

import cambo.freelance.webservice.models.AboutSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AboutSectionRepository extends JpaRepository<AboutSection, UUID> {
}