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
@Table(name = "topic")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String tid;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid")
    private Conference conference;
    @Column(name = "topic_name")
    private String topicName;

    public Topic() {
        //no-arg constructors
    }
}
