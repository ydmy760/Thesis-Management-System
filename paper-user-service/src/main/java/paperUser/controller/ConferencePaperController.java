package paperUser.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import paperUser.entity.Paper;
import paperUser.service.ConferencePaperService;
import paperUser.vo.PaperVo;
import paperUser.vo.Result;

import java.util.List;

@RestController
@RequestMapping("/api/conf/paper")
public class ConferencePaperController {
    @Autowired
    ConferencePaperService conferencePaperService;

    //投稿
    @PostMapping("/{uId}/{cId}")
    public Result submitPaper(@PathVariable(value = "uId") String uId, @PathVariable(value = "cId") String cId, @RequestBody PaperVo paperVo) {
        conferencePaperService.submitPaper(uId, cId, paperVo);
        return Result.success();
    }

    //获取投稿
    @GetMapping("/{uId}")
    public Result getPaperByUId(@PathVariable(value = "uId") String uId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Paper> paperList = conferencePaperService.getPaperByUId(uId, username);
        return Result.success(paperList);
    }

    //修改投稿
    @PutMapping("")
    public Result revisePaper(@RequestBody PaperVo paperVo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        conferencePaperService.revisePaper(paperVo, username);
        return Result.success();
    }

    //撤回投稿
    @DeleteMapping("/{pId}")
    public Result withdrawPaper(@PathVariable(value = "pId") String pId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        conferencePaperService.withdrawPaper(pId, username);
        return Result.success();
    }


}
