package cambo.freelance.webservice.services;

import cambo.freelance.webservice.models.ContactInfo;

import java.util.List;
import java.util.UUID;

public interface ContactInfoService {
    List<ContactInfo> findAll();
    ContactInfo findById(UUID id);
    ContactInfo save(ContactInfo contactInfo);
    ContactInfo update(UUID id, ContactInfo contactInfo);
    void delete(UUID id);
}