package paperUser.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import paperUser.entity.Invitation;
import paperUser.entity.User;
import paperUser.entity.enums.Role;
import paperUser.service.ConferenceUserService;
import paperUser.vo.CandidateListVo;
import paperUser.vo.InvitationResultVo;
import paperUser.vo.InvitationVo;
import paperUser.vo.Result;

import java.util.List;

@RestController
@RequestMapping("/api/conf/user")
public class ConferenceUserController {
    private final ConferenceUserService conferenceUserService;

    @Autowired
    public ConferenceUserController(ConferenceUserService conferenceUserService) {
        this.conferenceUserService = conferenceUserService;
    }

    //查询所有可邀请用户
    @GetMapping("/{cId}")
    public Result getUserByName(@PathVariable(value = "cId") String cId) {
        List<User> users = conferenceUserService.getUserByCId(cId);
        return Result.success(users);
    }

    //查询用户
    @GetMapping("/{name}/{cId}")
    public Result getUserByName(@PathVariable(value = "name") String name, @PathVariable(value = "cId") String cId) {
        List<User> users = conferenceUserService.getUserByName(name, cId);
        return Result.success(users);
    }

    //查询用户的会议角色
    @GetMapping("/role/{cId}/{uId}")
    public Result getRole(@PathVariable(value = "cId") String cId, @PathVariable(value = "uId") String uId) {
        Role role = conferenceUserService.getRole(cId, uId);
        return Result.success(role);
    }

    //邀请会议PC member
    @PostMapping("/invite")
    public Result invitePcMembers(@RequestBody CandidateListVo candidateListVo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        conferenceUserService.invitePcMembers(candidateListVo.getCid(), candidateListVo.getCandidates(), username);
        return Result.success();
    }

    //查询邀请状态
    @GetMapping("/invite/{cId}")
    public Result checkInvitationStateByCId(@PathVariable(value = "cId") String cId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Invitation> invitations = conferenceUserService.checkInvitationStateByCId(cId, username);
        return Result.success(invitations);
    }

    //撤回邀请: 仅支持撤回未处理的邀请
    @DeleteMapping("/invite/{id}")
    public Result withdrawInvitation(@PathVariable(value = "id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        conferenceUserService.withdrawInvitation(id, userId);
        return Result.success();
    }

    //查看PC member邀请
    @GetMapping("/recv-invitation/{uId}")
    public Result checkInvitationStateByUId(@PathVariable(value = "uId") String uId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        InvitationResultVo invitationResultVo = conferenceUserService.checkInvitationStateByUId(uId, username);
        return Result.success(invitationResultVo);
    }

    //处理PC member邀请
    @PutMapping("/recv-invitation")
    public Result processInvitation(@RequestBody InvitationVo invitationVo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        conferenceUserService.processInvitation(invitationVo.getId(), invitationVo.isChoice(), username, invitationVo.getTid());
        return Result.success();
    }

}
