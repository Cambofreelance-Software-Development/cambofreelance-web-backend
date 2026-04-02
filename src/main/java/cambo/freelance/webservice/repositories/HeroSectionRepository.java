package cambo.freelance.webservice.repositories;

import cambo.freelance.webservice.models.HeroSection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HeroSectionRepository extends JpaRepository<HeroSection, UUID> {
}