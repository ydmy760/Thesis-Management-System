package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperUser.entity.Conference;
import paperUser.entity.User;
import paperUser.entity.enums.ConferenceState;

import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, String> {
    List<Conference> findAllByConferenceStateNot(ConferenceState state2);

    List<Conference> findAllByChair(User user);

    Conference findByCid(String cid);
}
