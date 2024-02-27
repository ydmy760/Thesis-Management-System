package paperUser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paperUser.service.ReviewService;
import paperUser.service.StartReviewService;
import paperUser.vo.Result;

@RestController
@RequestMapping("/api/conf/review")
public class StartReviewController {
    @Autowired
    StartReviewService startReviewService;

    //开启审稿：chair开启会议的审稿状态
    //参数：当前会议的cid、前端选择的稿件分配策略strategy（1表示基于topic，2表示基于平均负担）
    //返回：返回成功开启审稿的信息
    @GetMapping("/start/{cid}")
    public Result releaseReview(@PathVariable(value = "cid") String cid, int strategy) {
        if(startReviewService.startReview(cid, strategy))
            return Result.success();
        else
            return new Result(0, "开启审稿失败", null);
    }
}
