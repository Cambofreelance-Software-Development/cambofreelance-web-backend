package cambo.freelance.webservice.services;

import cambo.freelance.webservice.models.ContactMessage;

import java.util.List;
import java.util.UUID;

public interface ContactMessageService {
    List<ContactMessage> findAll();
    List<ContactMessage> findByStatus(String status);
    ContactMessage findById(UUID id);
    ContactMessage save(ContactMessage message);
    ContactMessage updateStatus(UUID id, String status);
    void delete(UUID id);
}