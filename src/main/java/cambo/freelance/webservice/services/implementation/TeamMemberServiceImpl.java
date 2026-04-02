package cambo.freelance.webservice.services.implementation;

import cambo.freelance.webservice.models.TeamMember;
import cambo.freelance.webservice.repositories.TeamMemberRepository;
import cambo.freelance.webservice.services.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    public List<TeamMember> findAll() {
        return teamMemberRepository.findAll();
    }

    @Override
    public TeamMember findById(UUID id) {
        return teamMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamMember not found"));
    }

    @Override
    public TeamMember save(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    @Override
    public TeamMember update(UUID id, TeamMember teamMember) {
        TeamMember existing = findById(id);
        existing.setName(teamMember.getName());
        existing.setPosition(teamMember.getPosition());
        existing.setBio(teamMember.getBio());
        existing.setImageUrl(teamMember.getImageUrl());
        existing.setLinkedinUrl(teamMember.getLinkedinUrl());
        existing.setTwitterUrl(teamMember.getTwitterUrl());
        return teamMemberRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        teamMemberRepository.deleteById(id);
    }
}