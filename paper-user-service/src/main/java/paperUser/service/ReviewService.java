package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.Paper;
import paperUser.vo.CommentResultVo;

import java.util.List;

@Service
public interface ReviewService {
    void releaseReview(String cid);

    void confirmReview(String commentId, int rate);

    CommentResultVo getReviewByPaper(String pid);

    void submitRebuttal(String pid, String rebuttal);

    List<Paper> finalVerdict(String cid);
}
