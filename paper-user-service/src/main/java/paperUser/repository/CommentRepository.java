package paperUser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paperUser.entity.Comment;
import paperUser.entity.Paper;
import paperUser.entity.Referee;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByRefereeIn(List<Referee> referees);

    List<Comment> findByPaper(Paper paper);

    List<Comment> findByRefereeAndSubmit(Referee referee, boolean submit);

    List<Comment> findByPaperAndSubmitAndConfirm(Paper paper, boolean submit, boolean confirm);
}
