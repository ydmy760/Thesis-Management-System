package paperUser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import paperUser.entity.enums.Confidence;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "comment")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
public class Comment {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String comment_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "referee_id")
    private Referee referee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paper paper;

    @Column(name = "rate")
    private int rate;
    @Column(name = "content")
    private String content;
    @Column(name = "confidence")
    private Confidence confidence;
    @Column(name = "confirm")
    private boolean confirm;
    @Column(name = "submit", columnDefinition = "boolean default false")
    private boolean submit;

    public Comment() {
        // no-arg constructor
    }
}
