package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperUser.entity.Conference;
import paperUser.entity.Topic;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {
    List<Topic> findByConference(Conference conference);
}
