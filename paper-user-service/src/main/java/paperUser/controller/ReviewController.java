package paperUser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paperUser.entity.Paper;
import paperUser.service.ReviewService;
import paperUser.vo.CommentResultVo;
import paperUser.vo.ConfirmVo;
import paperUser.vo.RebuttalVo;
import paperUser.vo.Result;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    //发布初审结果
    @GetMapping("/release/{cid}")
    public Result releaseReview(@PathVariable(value = "cid") String cid) {
        reviewService.releaseReview(cid);
        return Result.success();
    }

    //确认评分
    @PostMapping("/confirm")
    public Result confirmReview(@RequestBody ConfirmVo confirmVo) {
        reviewService.confirmReview(confirmVo.getCommentId(), confirmVo.getRate());
        return Result.success();
    }

    //获取论文的评论
    @GetMapping("/{pid}")
    public Result getReviewByPaper(@PathVariable(value = "pid") String pid) {
        CommentResultVo commentResultVo = reviewService.getReviewByPaper(pid);
        return Result.success(commentResultVo);
    }

    //提交Rebuttal信息
    @PostMapping("/rebuttal")
    public Result submitRebuttal(@RequestBody RebuttalVo rebuttalVo) {
        reviewService.submitRebuttal(rebuttalVo.getPid(), rebuttalVo.getRebuttal());
        return Result.success();
    }

    //发布最终结果
    @GetMapping("/finalVerdict/{cid}")
    public Result finalVerdict(@PathVariable(value = "cid") String cid) {
        List<Paper> paperList = reviewService.finalVerdict(cid);
        return Result.success(paperList);
    }

}
