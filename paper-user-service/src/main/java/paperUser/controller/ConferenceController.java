package paperUser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paperUser.entity.Conference;
import paperUser.entity.Topic;
import paperUser.service.ConferenceService;
import paperUser.vo.ConferenceVo;
import paperUser.vo.Result;

import java.util.List;

@RestController
@RequestMapping("/api/conf/meeting")
public class ConferenceController {
    @Autowired
    ConferenceService conferenceService;

    @RequestMapping(value = "/getAllTopic/{cid}", method = RequestMethod.GET)
    public List<Topic> getAllTopic(@PathVariable(value = "cid") String cid) {
        return conferenceService.getAllTopic(cid);
    }

    @RequestMapping(value = "/getMeeting/{cid}", method = RequestMethod.GET)
    public Conference getMeeting(@PathVariable(value = "cid") String cid) {
        return conferenceService.getMeeting(cid);
    }

    @RequestMapping(value = "/getAllMeetings", method = RequestMethod.GET)
    public List<Conference> getAll() {
        return conferenceService.getAllConference();
    }

    @RequestMapping(value = "/getAllMyMeetings", method = RequestMethod.GET)
    public List<Conference> getMyAll() {
        return conferenceService.getAllMyConference();
    }

    @RequestMapping(value = "/applyMeeting", method = RequestMethod.POST)
    public Result applyMeeting(@RequestBody ConferenceVo conferenceVo) {
        conferenceService.applyConference(conferenceVo);
        return Result.success();
    }

    @RequestMapping(value = "/modifyMeeting", method = RequestMethod.POST)
    public Result modifyMeeting(@RequestBody Conference conference) {
        conferenceService.modifyConference(conference);
        return Result.success();
    }

    @RequestMapping(value = "/modifyState", method = RequestMethod.POST)
    public Result modifyState(@RequestBody Conference conference) {
        conferenceService.modifyState(conference);
        return Result.success();
    }

    @RequestMapping(value = "/getMyPcMeetings", method = RequestMethod.GET)
    public List<Conference> getMyPCMeetings() {
        return conferenceService.getMyPCMeetings();
    }
}
