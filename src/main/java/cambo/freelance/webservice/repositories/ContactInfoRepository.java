package cambo.freelance.webservice.repositories;

import cambo.freelance.webservice.models.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, UUID> {
}