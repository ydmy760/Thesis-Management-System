package paperUser.vo;

import lombok.Data;
import paperUser.entity.Conference;
import paperUser.entity.Topic;

import java.util.List;

@Data
public class ConferenceVo {
    Conference conference;
    List<String> topics;
}
