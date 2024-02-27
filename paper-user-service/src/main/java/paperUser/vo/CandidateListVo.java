package paperUser.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CandidateListVo {
    @JsonProperty(value = "cid")
    String cid;

    List<String> candidates;
}
