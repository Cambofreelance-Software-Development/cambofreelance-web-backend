package cambo.freelance.webservice.controllers.rest;

import cambo.freelance.webservice.configuration.constants.Constant;
import cambo.freelance.webservice.controllers.BaseController;
import cambo.freelance.webservice.exceptions.MessageDataResponse;
import cambo.freelance.webservice.models.Course;
import cambo.freelance.webservice.models.TeamMember;
import cambo.freelance.webservice.services.CourseService;
import cambo.freelance.webservice.services.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constant.API_VERSION_V1 + Constant.TEAM_MEMBER_PATH)
public class TeamMemberController extends BaseController {
    private final TeamMemberService teamMemberService;

    @GetMapping
    public ResponseEntity<?> getAllHeroSections() {
        messageDataResponse = new MessageDataResponse();
        List<TeamMember> teamMemberList = teamMemberService.findAll();
         messageDataResponse.getDataSuccess(teamMemberList);
         return ResponseEntity.ok(messageDataResponse);
    }
}
