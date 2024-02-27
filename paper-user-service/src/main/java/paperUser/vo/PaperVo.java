package paperUser.vo;

import lombok.Data;
import paperUser.entity.Author;
import paperUser.entity.Paper;
import paperUser.entity.Topic;

import java.util.List;

@Data
public class PaperVo {
    Paper paper;
    List<Author> authorList;
    List<Topic> topicList;
}
