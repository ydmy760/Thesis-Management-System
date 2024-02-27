package paperUser.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import paperUser.entity.Conference;
import paperUser.entity.Referee;
import paperUser.entity.Topic;
import paperUser.entity.User;
import paperUser.entity.enums.ConferenceState;
import paperUser.exception.WebException;
import paperUser.repository.ConferenceRepository;
import paperUser.repository.RefereeRepository;
import paperUser.repository.TopicRepository;
import paperUser.repository.UserRepository;
import paperUser.service.ConferenceService;
import paperUser.vo.ConferenceVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ConferenceImpl implements ConferenceService {
    private final ConferenceRepository conferenceRepository;

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final RefereeRepository refereeRepository;

    @Autowired
    public ConferenceImpl(ConferenceRepository conferenceRepository, UserRepository userRepository,
                          TopicRepository topicRepository, RefereeRepository refereeRepository) {
        this.conferenceRepository = conferenceRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.refereeRepository = refereeRepository;
    }

    @Override
    public List<Conference> getAllConference() {
        return conferenceRepository.findAllByConferenceStateNot(
                ConferenceState.UNDER_REVIEW
        );
//        return conferenceRepository.findAll();
    }

    @Override
    public List<Conference> getAllMyConference() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<User> user = userRepository.findByUsername(username);
        if (user.size() == 0) return null;
        return conferenceRepository.findAllByChair(user.get(0));
    }

    @Override
    public boolean applyConference(ConferenceVo conferenceVo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<User> user = userRepository.findByUsername(username);
        if (user.size() == 0) {
            throw new WebException("用户不存在");
        }
        Conference conference = conferenceVo.getConference();
        conference.setChair(user.get(0));
        conference.setConferenceState(ConferenceState.UNDER_REVIEW);
        conference = conferenceRepository.save(conference);
        List<String> topicList = conferenceVo.getTopics();
        for (String topicName : topicList) {
            Topic topic = new Topic();
            topic.setConference(conference);
            topic.setTopicName(topicName);
            topicRepository.save(topic);
        }
        return true;
    }


    @Override
    public boolean modifyConference(Conference conference) {
        Conference oldConference = conferenceRepository.findById(conference.getCid())
                .orElseThrow(() -> new WebException("会议不存在"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        if (!oldConference.getChair().getUsername().equals(username) || oldConference.getConferenceState() != ConferenceState.UNDER_REVIEW) {
            throw new WebException("不可更改");
        }
        conference.setChair(oldConference.getChair());
        conference.setConferenceState(ConferenceState.UNDER_REVIEW);
        conferenceRepository.save(conference);
        return true;
    }

    @Override
    public boolean modifyState(Conference conference) {
        Conference oldConference = conferenceRepository.findById(conference.getCid())
                .orElseThrow(() -> new WebException("会议不存在"));
        if (oldConference.getConferenceState() == ConferenceState.UNDER_REVIEW ||
                oldConference.getConferenceState() == ConferenceState.NOT_APPROVED ||
                conference.getConferenceState() == ConferenceState.NOT_APPROVED ||
                conference.getConferenceState() == ConferenceState.APPROVED) {
            throw new WebException("此状态下不可更改");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<User> user = userRepository.findByUsername(username);
        if (user.size() == 0 || !Objects.equals(username, oldConference.getChair().getUsername())) {
            throw new WebException("用户不可更改");
        }
        oldConference.setConferenceState(conference.getConferenceState());
        conferenceRepository.save(oldConference);
        return true;
    }

    @Override
    public List<Topic> getAllTopic(String cid) {
        Conference conference = conferenceRepository.findById(cid)
                .orElseThrow(() -> new WebException("会议不存在"));
        return topicRepository.findByConference(conference);
    }

    @Override
    public Conference getMeeting(String cid) {
        Conference conference = conferenceRepository.findById(cid)
                .orElseThrow(() -> new WebException("会议不存在"));
        return conference;
    }

    @Override
    public List<Conference> getMyPCMeetings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<User> user = userRepository.findByUsername(username);
        if (user.size() == 0) return null;
        List<Referee> referees = refereeRepository.findByUser(user.get(0));
        List<Conference> conferences = new ArrayList<>();
        for (Referee referee : referees) {
            conferences.add(referee.getConference());
        }
        return conferences;
    }
}
