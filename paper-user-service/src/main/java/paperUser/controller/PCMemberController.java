package paperUser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import paperUser.entity.Paper;
import paperUser.entity.enums.Confidence;
import paperUser.service.PCService;
import paperUser.vo.Result;
import paperUser.vo.SubmitCommentVo;

import java.util.List;

@RestController
public class PCMemberController {
    @Autowired
    PCService pcService;

    @RequestMapping(value = "/getReferPaper", method = RequestMethod.GET)
    Result getMyPapers(String cid) {
        List<Paper> list = pcService.getMyPaper(cid);
        return Result.success(list);
    }

    @RequestMapping(value = "/submitMessage", method = RequestMethod.POST)
    Result submitMessage(@RequestBody SubmitCommentVo submitCommentVo) {
        int rate = submitCommentVo.getRate();
        String comment = submitCommentVo.getComment();
        Confidence confidence = submitCommentVo.getConfidence();
        String pid = submitCommentVo.getPid();
        boolean res = pcService.submitMessage(rate, pid, comment, confidence);
        return Result.success(res);
    }
}
