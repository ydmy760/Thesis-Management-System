package paperUser.vo;

import lombok.Data;
import paperUser.entity.enums.Confidence;

@Data
public class SubmitCommentVo {
    int rate;
    String pid;
    String comment;
    Confidence confidence;
}
