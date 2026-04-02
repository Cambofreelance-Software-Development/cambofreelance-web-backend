package cambo.freelance.webservice.services;

import cambo.freelance.webservice.models.HeroSection;

import java.util.List;
import java.util.UUID;

public interface HeroSectionService {
    List<HeroSection> findAll();
    HeroSection findById(UUID id);
    HeroSection save(HeroSection heroSection);
    HeroSection update(UUID id, HeroSection heroSection);
    void delete(UUID id);
}