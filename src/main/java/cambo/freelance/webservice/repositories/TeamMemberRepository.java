package cambo.freelance.webservice.repositories;

import cambo.freelance.webservice.models.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
}