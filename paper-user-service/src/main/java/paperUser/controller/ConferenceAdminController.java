package paperUser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paperUser.entity.Conference;
import paperUser.service.ConferenceAdminService;
import paperUser.vo.AuditVo;
import paperUser.vo.Result;

import java.util.List;

@RestController
@RequestMapping("/software-admin-server/api/conf/admin")
public class ConferenceAdminController {
    @Autowired
    ConferenceAdminService conferenceAdminService;

    //管理员审批会议
    @PostMapping("/audit")
    public Result auditConference(@RequestBody AuditVo auditVo) {
        conferenceAdminService.auditConference(auditVo.getCId(), auditVo.getState());
        return Result.success();
    }

    //管理员获取所有待处理会议
    @GetMapping("/getAllMeetings")
    public Result getAllMeetings() {
        List<Conference> conferences = conferenceAdminService.getAllMeetings();
        return Result.success(conferences);
    }

}
