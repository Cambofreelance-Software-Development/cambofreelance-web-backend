package cambo.freelance.webservice.services;

import cambo.freelance.webservice.models.AboutSection;

import java.util.List;
import java.util.UUID;

public interface AboutSectionService {
    List<AboutSection> findAll();
    AboutSection findById(UUID id);
    AboutSection save(AboutSection aboutSection);
    AboutSection update(UUID id, AboutSection aboutSection);
    void delete(UUID id);
}