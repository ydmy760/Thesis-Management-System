package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperUser.entity.Conference;
import paperUser.entity.Participate;
import paperUser.entity.User;
import paperUser.entity.enums.Role;

import java.util.List;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, String> {
    Participate findByConferenceAndUser(Conference conference, User user);
    List<Participate> findByConference(Conference conference);
}
