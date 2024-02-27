package paperUser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "relate")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relate {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String relate_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paper paper;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tid")
    private Topic topic;

    public Relate() {
        //no-arg constructor
    }
}
