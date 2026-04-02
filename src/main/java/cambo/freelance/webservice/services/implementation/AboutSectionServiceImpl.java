package cambo.freelance.webservice.services.implementation;

import cambo.freelance.webservice.models.AboutSection;
import cambo.freelance.webservice.repositories.AboutSectionRepository;
import cambo.freelance.webservice.services.AboutSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AboutSectionServiceImpl implements AboutSectionService {

    private final AboutSectionRepository aboutSectionRepository;

    @Override
    public List<AboutSection> findAll() {
        return aboutSectionRepository.findAll();
    }

    @Override
    public AboutSection findById(UUID id) {
        return aboutSectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AboutSection not found"));
    }

    @Override
    public AboutSection save(AboutSection aboutSection) {
        return aboutSectionRepository.save(aboutSection);
    }

    @Override
    public AboutSection update(UUID id, AboutSection aboutSection) {
        AboutSection existing = findById(id);
        existing.setTitle(aboutSection.getTitle());
        existing.setContent(aboutSection.getContent());
        existing.setImageUrl(aboutSection.getImageUrl());
        return aboutSectionRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        aboutSectionRepository.deleteById(id);
    }
}