package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperUser.entity.Conference;
import paperUser.entity.Referee;
import paperUser.entity.User;

import java.util.List;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, String> {
    Referee findByConferenceAndUser(Conference conference, User user);

    List<Referee> findByConference(Conference conference);

    List<Referee> findByUser(User user);
}
