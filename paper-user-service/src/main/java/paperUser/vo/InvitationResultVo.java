package paperUser.vo;

import lombok.Data;
import paperUser.entity.Invitation;

import java.util.ArrayList;
import java.util.List;

@Data
public class InvitationResultVo {
    List<Invitation> pending;
    List<Invitation> accept;
    List<Invitation> refuse;

    public InvitationResultVo() {
        pending = new ArrayList<>();
        accept = new ArrayList<>();
        refuse = new ArrayList<>();
    }
}
