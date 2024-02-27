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
@Table(name = "author")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String author_id;
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "workplace")
    private String workplace;
    @Column(name = "region")
    private String region;
    @Column(name = "email")
    private String email;
    @Column(name = "author_order")
    private int order;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pid")
    private Paper paper;

    public Author() {
        //no-arg constructor
    }
}
