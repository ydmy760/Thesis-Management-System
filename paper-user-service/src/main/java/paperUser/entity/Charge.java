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
@Table(name = "charge")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Charge {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String charge_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "referee_id")
    private Referee referee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tid")
    private Topic topic;

    public Charge() {
        //no-arg constructor
    }
}
