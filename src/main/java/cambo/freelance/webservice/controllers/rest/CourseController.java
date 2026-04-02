package cambo.freelance.webservice.controllers.rest;

import cambo.freelance.webservice.configuration.constants.Constant;
import cambo.freelance.webservice.controllers.BaseController;
import cambo.freelance.webservice.exceptions.MessageDataResponse;
import cambo.freelance.webservice.models.projection.CourseFullView;
import cambo.freelance.webservice.models.projection.CourseView;
import cambo.freelance.webservice.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constant.API_VERSION_V1 + Constant.COURSE_PATH)
public class CourseController extends BaseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        messageDataResponse = new MessageDataResponse();
        List<CourseFullView> courseList = courseService.findAllProjectedBy();
         messageDataResponse.getDataSuccess(courseList);
         return ResponseEntity.ok(messageDataResponse);
    }

    @GetMapping("/test/{price}")
    public ResponseEntity<?> getFeaturedCourses(@PathVariable("price") BigDecimal price) {
        messageDataResponse = new MessageDataResponse();
        List<CourseView> courseList = courseService.findByPriceGreaterThan(price);
        messageDataResponse.getDataSuccess(courseList);
        return ResponseEntity.ok(messageDataResponse);
    }

    @GetMapping("/by-category/{id}")
    public ResponseEntity<?> getFeaturedCourses(@PathVariable("id") Integer id) {
        messageDataResponse = new MessageDataResponse();
        List<CourseFullView> courseList = courseService.findByCategoryId(id);
        messageDataResponse.getDataSuccess(courseList);
        return ResponseEntity.ok(messageDataResponse);
    }
}
