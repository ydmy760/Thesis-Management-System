package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.Comment;
import paperUser.entity.Paper;
import paperUser.entity.enums.Confidence;

import java.util.List;

@Service
public interface PCService {
    List<Paper> getMyPaper(String conferenceId);
    boolean submitMessage(int rate, String pid, String comment, Confidence confidence);
}
