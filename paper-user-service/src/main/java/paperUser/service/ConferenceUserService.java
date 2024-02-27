package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.Invitation;
import paperUser.entity.User;
import paperUser.entity.enums.Role;
import paperUser.vo.InvitationResultVo;

import java.util.List;

@Service
public interface ConferenceUserService {
    List<User> getUserByName(String name, String cId);

    void invitePcMembers(String cid, List<String> candidates, String username);

    List<Invitation> checkInvitationStateByCId(String cId, String username);

    void withdrawInvitation(String id, String username);

    InvitationResultVo checkInvitationStateByUId(String uId, String username);

    void processInvitation(String id, boolean choice, String username, List<String> tidList);

    Role getRole(String cId, String uId);

    List<User> getUserByCId(String cId);
}
