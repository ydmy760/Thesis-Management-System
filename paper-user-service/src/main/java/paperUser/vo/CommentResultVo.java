package paperUser.vo;

import lombok.Data;
import paperUser.entity.Comment;

import java.util.List;

@Data
public class CommentResultVo {
    List<Comment> comments;
    boolean accept;
}
