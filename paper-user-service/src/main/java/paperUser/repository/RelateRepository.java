package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperUser.entity.Paper;
import paperUser.entity.Relate;
import paperUser.entity.Topic;
import paperUser.entity.User;

import java.util.List;

public interface RelateRepository extends JpaRepository<Relate, String> {
    List<Relate> findByTopic(Topic topic);
    List<Relate> findByPaper(Paper paper);
}
