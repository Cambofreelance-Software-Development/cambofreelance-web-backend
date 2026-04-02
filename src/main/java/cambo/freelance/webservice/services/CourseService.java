package cambo.freelance.webservice.services;

import cambo.freelance.webservice.models.Course;
import cambo.freelance.webservice.models.projection.CourseFullView;
import cambo.freelance.webservice.models.projection.CourseView;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<Course> findAll();
    List<Course> findFeatured();
    Course findById(UUID id);
    Course save(Course course);
    Course update(UUID id, Course course);
    void delete(UUID id);
    List<CourseView> findByPriceGreaterThan(BigDecimal price);

    List<CourseFullView> findAllProjectedBy();

    List<CourseFullView> findByCategoryId(Integer id);
}