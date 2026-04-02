package cambo.freelance.webservice.repositories;

import cambo.freelance.webservice.models.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {

    // Example: find messages by status ("unread", "read", etc.)
    List<ContactMessage> findByStatus(String status);
}