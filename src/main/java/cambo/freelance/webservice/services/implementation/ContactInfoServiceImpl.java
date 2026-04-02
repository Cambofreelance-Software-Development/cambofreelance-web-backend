package cambo.freelance.webservice.services.implementation;

import cambo.freelance.webservice.models.ContactInfo;
import cambo.freelance.webservice.repositories.ContactInfoRepository;
import cambo.freelance.webservice.services.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    @Override
    public List<ContactInfo> findAll() {
        return contactInfoRepository.findAll();
    }

    @Override
    public ContactInfo findById(UUID id) {
        return contactInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ContactInfo not found"));
    }

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    @Override
    public ContactInfo update(UUID id, ContactInfo contactInfo) {
        ContactInfo existing = findById(id);
        existing.setEmail(contactInfo.getEmail());
        existing.setPhone(contactInfo.getPhone());
        existing.setAddress(contactInfo.getAddress());
        existing.setOfficeHours(contactInfo.getOfficeHours());
        return contactInfoRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        contactInfoRepository.deleteById(id);
    }
}