package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.Conference;

import java.util.List;

@Service
public interface ConferenceAdminService {
    void auditConference(String cId, int state);

    List<Conference> getAllMeetings();
}
