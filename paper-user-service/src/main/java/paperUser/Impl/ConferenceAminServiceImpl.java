package paperUser.Impl;

import com.google.protobuf.Empty;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import paperUser.entity.Conference;
import paperUser.entity.enums.ConferenceState;
import paperUser.service.ConferenceAdminService;
//import proto.conf.ConfList;
//import proto.conf.ConfServiceGrpc;
//import proto.conf.GetConfAuditState;
//import proto.conf.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConferenceAminServiceImpl implements ConferenceAdminService {
//    @GrpcClient("software-admin-server")
//    ConfServiceGrpc.ConfServiceBlockingStub confServiceBlockingStub;

    @Override
    public void auditConference(String cId, int state) {
//        GetConfAuditState getConfAuditState = GetConfAuditState.newBuilder().setState(state).setCId(cId).build();
//        confServiceBlockingStub.auditConf(getConfAuditState);
    }

    @Override
    public List<Conference> getAllMeetings() {
        List<Conference> conferences = new ArrayList<>();
//        ConfList confList = confServiceBlockingStub.getAllConf(Empty.newBuilder().build());
//        for (proto.conf.Conference conference : confList.getConfList()) {
//            User user = conference.getChair();
//            paperUser.entity.User res_user = new paperUser.entity.User();
//            res_user.setAdmin(user.getAdmin());
//            res_user.setUid(user.getUid());
//            res_user.setEmail(user.getEmail());
//            res_user.setWorkplace(user.getWorkplace());
//            res_user.setUsername(user.getUsername());
//            res_user.setPassword(user.getPassword());
//            res_user.setFullName(user.getFullName());
//            res_user.setRegion(user.getRegion());
//            Conference res_conference = new Conference();
//            res_conference.setCid(conference.getCid());
//            res_conference.setChair(res_user);
//            res_conference.setDate(new Date(conference.getDate().getSeconds() * 1000));
//            res_conference.setBrief(conference.getBrief());
//            res_conference.setName(conference.getName());
//            res_conference.setPlace(conference.getPlace());
//            res_conference.setAccept_date(new Date(conference.getAcceptDate().getSeconds() * 1000));
//            res_conference.setSubmit_date(new Date(conference.getSubmitDate().getSeconds() * 1000));
//            res_conference.setConferenceState(ConferenceState.values()[conference.getConferenceStateValue()]);
//            conferences.add(res_conference);
//        }
        return conferences;
    }
}
