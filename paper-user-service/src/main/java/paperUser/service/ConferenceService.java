package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.Conference;
import paperUser.entity.Topic;
import paperUser.vo.ConferenceVo;

import java.util.List;

@Service
public interface ConferenceService {
    List<Conference> getAllConference();

    List<Conference> getAllMyConference();

    boolean applyConference(ConferenceVo conference);

    boolean modifyConference(Conference conference);

    boolean modifyState(Conference conference);

    List<Topic> getAllTopic(String cid);

    Conference getMeeting(String cid);

    List<Conference> getMyPCMeetings();
}