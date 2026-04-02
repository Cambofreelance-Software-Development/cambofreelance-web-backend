package cambo.freelance.webservice.services;

import cambo.freelance.webservice.models.TeamMember;

import java.util.List;
import java.util.UUID;

public interface TeamMemberService {
    List<TeamMember> findAll();
    TeamMember findById(UUID id);
    TeamMember save(TeamMember teamMember);
    TeamMember update(UUID id, TeamMember teamMember);
    void delete(UUID id);
}