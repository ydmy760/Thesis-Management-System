package paperUser.Impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import paperUser.entity.*;
import paperUser.entity.enums.Confidence;
import paperUser.exception.WebException;
import paperUser.repository.*;
import paperUser.service.PCService;

import java.util.ArrayList;
import java.util.List;

@Service
public class PCServiceImpl implements PCService {
    private final CommentRepository commentRepository;
    private final RefereeRepository refereeRepository;
    private final ConferenceRepository conferenceRepository;
    private final UserRepository userRepository;
    private final PaperRepository paperRepository;

    public PCServiceImpl(CommentRepository commentRepository, RefereeRepository refereeRepository, ConferenceRepository conferenceRepository, UserRepository userRepository, PaperRepository paperRepository) {
        this.commentRepository = commentRepository;
        this.refereeRepository = refereeRepository;
        this.conferenceRepository = conferenceRepository;
        this.userRepository = userRepository;
        this.paperRepository = paperRepository;
    }

    @Override
    public List<Paper> getMyPaper(String conferenceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<User> user = userRepository.findByUsername(username);
        Conference conference = conferenceRepository.findByCid(conferenceId);
        if (user.size() == 0) return null;
        Referee referee = refereeRepository.findByConferenceAndUser(conference, user.get(0));
        // 返回submit字段为false的comments，从中找到对应的paper
        List<Comment> comments = commentRepository.findByRefereeAndSubmit(referee, false);
        List<Paper> res = new ArrayList<>();
        comments.forEach(item -> {
            res.add(item.getPaper());
        });
        return res;
    }

    @Override
    public boolean submitMessage(int rate, String pid, String content, Confidence confidence) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<User> user = userRepository.findByUsername(username);
        if (user.size() == 0) return false;

        // 该论文的实体对象
        Paper paper = paperRepository.findById(pid)
                .orElseThrow(() -> new WebException("不存在该论文"));
        // 这个referr是”我“
        Referee referee = refereeRepository.findByConferenceAndUser(paper.getConference(), user.get(0));
        // 分配给我的，我未审核的记录
        List<Comment> comments = commentRepository.findByRefereeAndSubmit(referee, false);

        for(Comment comment : comments) {
            // 更新对应的comment
            if(comment.getPaper() == paper) {
                comment.setRate(rate);
                comment.setConfidence(confidence);
                comment.setContent(content);
                comment.setSubmit(true);
                commentRepository.save(comment);
                break;
            }
        }
        // 查找所有已提交审核的记录，当审核数量达到3时，将每一个comment记录进行确认
        List<Comment> commentList = commentRepository.findByPaperAndSubmitAndConfirm(paper, true, false);
        if(commentList.size() == 3) {
            for(Comment comment : commentList) {
                comment.setConfirm(true);
                commentRepository.save(comment);
            }
        }
        return true;
    }
}
