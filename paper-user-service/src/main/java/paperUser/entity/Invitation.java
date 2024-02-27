package paperUser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import paperUser.entity.enums.InvitationState;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "invitation")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
public class Invitation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String invitation_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid")
    private Conference conference;

    @Column(name = "invitation_state")
    @Enumerated(EnumType.ORDINAL)
    private InvitationState invitationState;

    @Transient
    private List<Topic> topicList;

    public Invitation() {
        //no-arg constructor
    }


}
