package paperUser.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paperUser.entity.Comment;
import paperUser.entity.Conference;
import paperUser.entity.Paper;
import paperUser.entity.Referee;
import paperUser.entity.enums.ConferenceState;
import paperUser.entity.enums.PaperState;
import paperUser.exception.WebException;
import paperUser.repository.CommentRepository;
import paperUser.repository.ConferenceRepository;
import paperUser.repository.PaperRepository;
import paperUser.repository.RefereeRepository;
import paperUser.service.ReviewService;
import paperUser.vo.CommentResultVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ConferenceRepository conferenceRepository;
    private CommentRepository commentRepository;
    private RefereeRepository refereeRepository;
    private PaperRepository paperRepository;

    @Autowired
    public ReviewServiceImpl(ConferenceRepository conferenceRepository, CommentRepository commentRepository,
                             RefereeRepository refereeRepository, PaperRepository paperRepository) {
        this.conferenceRepository = conferenceRepository;
        this.commentRepository = commentRepository;
        this.refereeRepository = refereeRepository;
        this.paperRepository = paperRepository;
    }

    @Override
    public void releaseReview(String cid) {
        Conference conference = conferenceRepository.findById(cid)
                .orElseThrow(() -> new WebException("会议不存在"));
        List<Referee> referees = refereeRepository.findByConference(conference);
        List<Comment> comments = commentRepository.findByRefereeIn(referees);
        for (Comment comment : comments) {
            if (!comment.isSubmit()) {
                throw new WebException("存在已分配未审稿的稿件");
            }
        }
        conference.setConferenceState(ConferenceState.FINAL_REVIEW);
        conferenceRepository.save(conference);
    }

    @Override
    public void confirmReview(String commentId, int rate) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new WebException("评论不存在"));
        if (!comment.isSubmit()) {
            throw new WebException("评论未提交");
        }
        if (comment.isConfirm()) {
            throw new WebException("评论已确认");
        }
        comment.setRate(rate);
        comment.setConfirm(true);
        commentRepository.save(comment);
    }

    @Override
    public CommentResultVo getReviewByPaper(String pid) {
        Paper paper = paperRepository.findById(pid)
                .orElseThrow(() -> new WebException("投稿不存在"));
        Conference conference = paper.getConference();
        if (conference.getConferenceState() != ConferenceState.FINAL_REVIEW) {
            throw new WebException("初审结果未发布");
        }
        if (paper.getPaperState() != PaperState.UNDER_REVIEWED) {
            throw new WebException("投稿初审未完成");
        }
        CommentResultVo commentResultVo = new CommentResultVo();
        List<Comment> comments = commentRepository.findByPaper(paper);
        commentResultVo.setComments(comments);
        commentResultVo.setAccept(checkAccept(comments));
        return commentResultVo;
    }

    private boolean checkAccept(List<Comment> comments) {
        boolean accept = true;
        for (Comment comment : comments) {
            int rate = comment.getRate();
            if (rate == -1 || rate == -2) {
                accept = false;
                break;
            }
        }
        return accept;
    }

    @Override
    public void submitRebuttal(String pid, String rebuttal) {
        Paper paper = paperRepository.findById(pid)
                .orElseThrow(() -> new WebException("投稿不存在"));
        if (paper.getRebuttal() != null) {
            throw new WebException("已提交rebuttal");
        }
        paper.setRebuttal(rebuttal);
        paperRepository.save(paper);
    }

    @Override
    public List<Paper> finalVerdict(String cid) {
        Conference conference = conferenceRepository.findById(cid)
                .orElseThrow(() -> new WebException("会议不存在"));
        List<Referee> referees = refereeRepository.findByConference(conference);
        List<Comment> comments = commentRepository.findByRefereeIn(referees);
        for (Comment comment : comments) {
            if (!comment.isConfirm()) {
                throw new WebException("存在未确认的评论");
            }
        }
        List<Paper> paperList = paperRepository.findByConference(conference);
        List<Paper> acceptPaper = new ArrayList<>();
        for (Paper paper : paperList) {
            if (paper.getPaperState() == PaperState.REVIEWED) {
                List<Comment> commentList = commentRepository.findByPaper(paper);
                if (checkAccept(commentList)) {
                    acceptPaper.add(paper);
                }
            }
        }
        conference.setConferenceState(ConferenceState.PEER_REVIEW_COMPLETED);
        return acceptPaper;
    }

}
