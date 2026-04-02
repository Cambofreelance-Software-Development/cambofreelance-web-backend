package cambo.freelance.webservice.repositories;

import cambo.freelance.webservice.models.Course;
import cambo.freelance.webservice.models.projection.CourseFullView;
import cambo.freelance.webservice.models.projection.CourseView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    // Example: find featured courses
    List<Course> findByFeaturedTrue();

    // Example: find by level
    List<Course> findByLevelIgnoreCase(String level);

    List<CourseView> findByPriceGreaterThan(BigDecimal priceIsGreaterThan);

    List<CourseFullView> findAllProjectedBy();

    List<CourseFullView> findByCategoriesId(Integer categoriesId);
}