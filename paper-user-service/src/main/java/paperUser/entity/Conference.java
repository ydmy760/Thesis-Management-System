package paperUser.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import paperUser.entity.enums.ConferenceState;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Table(name = "conference")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Conference {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String cid;
    @Column(name = "conf_name")
    // full name of the conference
    private String name;
    @Column(name = "conf_date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "brief")
    //abbreviation of the conference
    private String brief;
    @Column(name = "submit_date")
    @Temporal(TemporalType.DATE)
    private Date submit_date;
    @Column(name = "place")
    private String place;
    @Column(name = "accept_date")
    @Temporal(TemporalType.DATE)
    private Date accept_date;
    @Column(name = "conf_state")
    @Enumerated(EnumType.ORDINAL)
    private ConferenceState conferenceState;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chair_id")
    private User chair;


    public Conference() {
        // no-arg constructor
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Conference conference = (Conference) obj;
        return name.equals(conference.name)
                && date.equals(conference.date)
                && brief.equals(conference.brief)
                && submit_date.equals(conference.submit_date)
                && accept_date.equals(conference.accept_date)
                && conferenceState == conference.conferenceState;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((cid == null) ? 0 : cid.hashCode());
        return result;
    }


}
