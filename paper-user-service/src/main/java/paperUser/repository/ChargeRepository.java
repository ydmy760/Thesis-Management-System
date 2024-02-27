package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperUser.entity.Charge;
import paperUser.entity.Relate;
import paperUser.entity.Topic;

import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, String> {
    List<Charge> findByTopic(Topic topic);
}
