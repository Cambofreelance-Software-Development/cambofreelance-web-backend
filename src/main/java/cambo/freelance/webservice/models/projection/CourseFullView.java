package cambo.freelance.webservice.models.projection;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CourseFullView {
    UUID getId();
    String getTitle();
    String getPrice();
    String getDescription();
    String getDuration();
    String getLevel();
    String getImageUrl();
    Boolean getFeatured();
    LocalDate getStartingDate();
    List<LearningOutcomeView> getLearningOutcomes();
//    List<CategoryView> getCategories();
}