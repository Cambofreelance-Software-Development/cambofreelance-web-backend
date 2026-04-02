package cambo.freelance.webservice.services.implementation;

import cambo.freelance.webservice.models.HeroSection;
import cambo.freelance.webservice.repositories.HeroSectionRepository;
import cambo.freelance.webservice.services.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroSectionServiceImpl implements HeroSectionService {

    private final HeroSectionRepository heroSectionRepository;

    @Override
    public List<HeroSection> findAll() {
        return heroSectionRepository.findAll();
    }

    @Override
    public HeroSection findById(UUID id) {
        return heroSectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HeroSection not found"));
    }

    @Override
    public HeroSection save(HeroSection heroSection) {
        return heroSectionRepository.save(heroSection);
    }

    @Override
    public HeroSection update(UUID id, HeroSection heroSection) {
        HeroSection existing = findById(id);
        existing.setTitle(heroSection.getTitle());
        existing.setSubtitle(heroSection.getSubtitle());
        existing.setDescription(heroSection.getDescription());
        existing.setCtaText(heroSection.getCtaText());
        existing.setCtaLink(heroSection.getCtaLink());
        existing.setBackgroundImage(heroSection.getBackgroundImage());
        return heroSectionRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        heroSectionRepository.deleteById(id);
    }
}