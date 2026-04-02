package cambo.freelance.webservice.controllers.rest;

import cambo.freelance.webservice.configuration.constants.Constant;
import cambo.freelance.webservice.controllers.BaseController;
import cambo.freelance.webservice.exceptions.MessageDataResponse;
import cambo.freelance.webservice.models.HeroSection;
import cambo.freelance.webservice.services.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constant.API_VERSION_V1 + Constant.HERO_SECTION_PATH)
public class HeroSectionController extends BaseController {
    private final HeroSectionService heroSectionService;

    @GetMapping
    public ResponseEntity<?> getAllHeroSections() {
        messageDataResponse = new MessageDataResponse();
        List<HeroSection> heroSection = heroSectionService.findAll();
         messageDataResponse.getDataSuccess(heroSection);
         return ResponseEntity.ok(messageDataResponse);
    }
}
