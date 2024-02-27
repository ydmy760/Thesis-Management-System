package paperUser.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuditVo {
    @JsonProperty(value = "cId")
    String cId;

    int state;
}
