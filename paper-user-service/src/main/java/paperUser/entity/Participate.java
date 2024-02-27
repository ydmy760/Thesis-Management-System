package paperUser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import paperUser.entity.enums.Role;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "participate")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participate {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String participate_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid")
    private Conference conference;
    @Column(name = "user_role")
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    public Participate() {
        // no-arg constructor
    }


}
