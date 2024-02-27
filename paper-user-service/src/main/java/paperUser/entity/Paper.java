package paperUser.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import paperUser.entity.enums.PaperState;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "paper")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paper {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String pid;
    @Column(name = "title")
    private String title;
    @Column(name = "abstract")
    private String abstractInfo;
    @Column(name = "pdf")
    private String pdfUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid")
    private Conference conference;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    private User user;
    @Column(name = "rebuttal")
    private String rebuttal;
    @Column(name = "paper_state")
    @Enumerated(EnumType.ORDINAL)
    private PaperState paperState;
    @Transient
    private List<Author> authorList;


    public Paper() {
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
        Paper paper = (Paper) obj;
        return title.equals(paper.title)
                && abstractInfo.equals(paper.abstractInfo)
                && pdfUrl.equals(paper.pdfUrl);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((pid == null) ? 0 : pid.hashCode());
        return result;
    }

}
