package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperUser.entity.Conference;
import paperUser.entity.Invitation;
import paperUser.entity.User;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, String> {
    List<Invitation> findInvitationByConference(Conference conference);

    List<Invitation> findInvitationByUser(User user);

}
