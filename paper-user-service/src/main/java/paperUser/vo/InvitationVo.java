package paperUser.vo;

import lombok.Data;

import java.util.List;


@Data
public class InvitationVo {
    String id;
    boolean choice;
    List<String> tid;
}
