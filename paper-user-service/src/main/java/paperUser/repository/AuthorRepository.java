package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import paperUser.entity.Author;
import paperUser.entity.Paper;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, String> {
    List<Author> findByPaper(Paper paper);

}
