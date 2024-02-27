package paperUser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "referee")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Referee {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String referee_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid")
    private Conference conference;

    public Referee() {
        // no-arg constructor
    }


}
