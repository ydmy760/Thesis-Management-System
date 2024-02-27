package paperUser.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paperUser.entity.*;
import paperUser.entity.enums.InvitationState;
import paperUser.entity.enums.Role;
import paperUser.exception.WebException;
import paperUser.repository.*;
import paperUser.service.ConferenceUserService;
import paperUser.vo.InvitationResultVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConferenceUserServiceImpl implements ConferenceUserService {
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;
    private final RefereeRepository refereeRepository;
    private final TopicRepository topicRepository;
    private final ChargeRepository chargeRepository;

    @Autowired
    ConferenceUserServiceImpl(InvitationRepository invitationRepository, UserRepository userRepository,
                              ConferenceRepository conferenceRepository, RefereeRepository refereeRepository, TopicRepository topicRepository, ChargeRepository chargeRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.conferenceRepository = conferenceRepository;
        this.refereeRepository = refereeRepository;
        this.topicRepository = topicRepository;
        this.chargeRepository = chargeRepository;
    }

    @Override
    public List<User> getUserByName(String name, String cId) {
        Conference conference = conferenceRepository.findById(cId)
                .orElseThrow(() -> new WebException("会议不存在"));
        List<User> users = userRepository.findByFullNameContains(name);
        User chair = conference.getChair();
        for (User user : users) {
            if (chair.getUid().equals(user.getUid())) {
                users.remove(user);
            } else {
                Referee participate = refereeRepository.findByConferenceAndUser(conference, user);
                if (participate != null) {
                    users.remove(user);
                }
            }
        }
        return users;
    }

    @Override
    public void invitePcMembers(String cid, List<String> candidates, String username) {
        Conference conference = conferenceRepository.findById(cid)
                .orElseThrow(() -> new WebException("会议不存在"));
        if (!conference.getChair().getUsername().equals(username)) {
            throw new WebException("无权限");
        }
        List<User> users = new ArrayList<>();
        for (String uId : candidates) {
            User user = userRepository.findById(uId)
                    .orElseThrow(() -> new WebException("用户不存在"));
            users.add(user);
        }
        for (User user : users) {
            Invitation invitation = new Invitation();
            invitation.setConference(conference);
            invitation.setUser(user);
            invitation.setInvitationState(InvitationState.PENDING);
            invitationRepository.save(invitation);
        }
    }

    @Override
    public List<Invitation> checkInvitationStateByCId(String cId, String username) {
        Conference conference = conferenceRepository.findById(cId)
                .orElseThrow(() -> new WebException("会议不存在"));
        if (!conference.getChair().getUsername().equals(username)) {
            throw new WebException("无权限");
        }
        return invitationRepository.findInvitationByConference(conference);
    }

    @Override
    public void withdrawInvitation(String id, String username) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new WebException("邀请不存在"));
        if (invitation.getInvitationState() != InvitationState.PENDING) {
            throw new WebException("已处理的邀请无法撤回");
        }
        Conference conference = invitation.getConference();
        if (!conference.getChair().getUsername().equals(username)) {
            throw new WebException("无权限");
        }
        invitationRepository.delete(invitation);
    }

    @Override
    public InvitationResultVo checkInvitationStateByUId(String uId, String username) {
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new WebException("用户不存在"));
        if (!user.getUsername().equals(username)) {
            throw new WebException("用户不匹配");
        }
        List<Invitation> invitations = invitationRepository.findInvitationByUser(user);
        InvitationResultVo invitationResultVo = new InvitationResultVo();
        for (Invitation i : invitations) {
            Conference conference = i.getConference();
            List<Topic> topicList = topicRepository.findByConference(conference);
            i.setTopicList(topicList);
            switch (i.getInvitationState()) {
                case PENDING:
                    invitationResultVo.getPending().add(i);
                    break;
                case ACCEPT:
                    invitationResultVo.getAccept().add(i);
                    break;
                case REFUSE:
                    invitationResultVo.getRefuse().add(i);
                    break;
                default:
                    throw new WebException("无效邀请状态");
            }
        }
        return invitationResultVo;
    }

    @Override
    public void processInvitation(String id, boolean choice, String username, List<String> tidList) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new WebException("邀请不存在"));
        User user = invitation.getUser();
        if (!user.getUsername().equals(username)) {
            throw new WebException("用户不匹配");
        }
        if (invitation.getInvitationState() != InvitationState.PENDING) {
            throw new WebException("邀请已处理");
        }
        if (choice) {
            invitation.setInvitationState(InvitationState.ACCEPT);
            Referee referee = new Referee();
            Conference conference = invitation.getConference();
            referee.setConference(conference);
            referee.setUser(user);
            for (String tid : tidList) {
                Topic topic = topicRepository.findById(tid)
                        .orElseThrow(() -> new WebException("话题不存在"));
                Charge charge = new Charge();
                charge.setReferee(referee);
                charge.setTopic(topic);
                chargeRepository.save(charge);
            }
            invitationRepository.save(invitation);
            refereeRepository.save(referee);
        } else {
            invitation.setInvitationState(InvitationState.REFUSE);
            invitationRepository.save(invitation);
        }
    }

    @Override
    public Role getRole(String cId, String uId) {
        Conference conference = conferenceRepository.findById(cId)
                .orElseThrow(() -> new WebException("会议不存在"));
        if (conference.getChair().getUid().equals(uId)) {
            return Role.CHAIR;
        }
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new WebException("用户不存在"));
        Referee referee = refereeRepository.findByConferenceAndUser(conference, user);
        if (referee != null) {
            return Role.PC_MEMBER;
        } else {
            return null;
        }
    }

    @Override
    public List<User> getUserByCId(String cId) {
        Conference conference = conferenceRepository.findById(cId)
                .orElseThrow(() -> new WebException("会议不存在"));
        List<User> userList = userRepository.findAll();
        userList.remove(conference.getChair());
        List<Referee> participates = refereeRepository.findByConference(conference);
        for (Referee participate : participates) {
            userList.remove(participate.getUser());
        }
        return userList;
    }
}
