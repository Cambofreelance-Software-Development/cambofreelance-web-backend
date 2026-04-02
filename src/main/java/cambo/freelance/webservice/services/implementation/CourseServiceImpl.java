package cambo.freelance.webservice.services.implementation;

import cambo.freelance.webservice.models.Course;
import cambo.freelance.webservice.models.projection.CourseFullView;
import cambo.freelance.webservice.models.projection.CourseView;
import cambo.freelance.webservice.repositories.CourseRepository;
import cambo.freelance.webservice.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findFeatured() {
        return courseRepository.findByFeaturedTrue();
    }

    @Override
    public Course findById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course update(UUID id, Course course) {
        Course existing = findById(id);
        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setDuration(course.getDuration());
        existing.setLevel(course.getLevel());
        existing.setPrice(course.getPrice());
        existing.setImageUrl(course.getImageUrl());
        existing.setFeatured(course.getFeatured());
        return courseRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseView> findByPriceGreaterThan(BigDecimal price) {
        return courseRepository.findByPriceGreaterThan(price);
    }

    @Override
    public List<CourseFullView> findAllProjectedBy() {
        return courseRepository.findAllProjectedBy();
    }

    @Override
    public List<CourseFullView> findByCategoryId(Integer id) {
        if( id == null || id == 0 ){
            return findAllProjectedBy();
        }
        return courseRepository.findByCategoriesId(id);
    }
}