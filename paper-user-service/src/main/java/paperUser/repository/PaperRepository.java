package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperUser.entity.Conference;
import paperUser.entity.Paper;
import paperUser.entity.User;

import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, String> {
    List<Paper> findByUser(User user);

    List<Paper> findByConference(Conference conference);
}
