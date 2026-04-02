package cambo.freelance.webservice.services.implementation;

import cambo.freelance.webservice.models.ContactMessage;
import cambo.freelance.webservice.repositories.ContactMessageRepository;
import cambo.freelance.webservice.services.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    @Override
    public List<ContactMessage> findAll() {
        return contactMessageRepository.findAll();
    }

    @Override
    public List<ContactMessage> findByStatus(String status) {
        return contactMessageRepository.findByStatus(status);
    }

    @Override
    public ContactMessage findById(UUID id) {
        return contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ContactMessage not found"));
    }

    @Override
    public ContactMessage save(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    @Override
    public ContactMessage updateStatus(UUID id, String status) {
        ContactMessage message = findById(id);
        message.setStatus(status);
        return contactMessageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        contactMessageRepository.deleteById(id);
    }
}