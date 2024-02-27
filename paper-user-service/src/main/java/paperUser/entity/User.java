package paperUser.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@Entity
@Table(name = "user")
@Getter
@Setter
@GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String uid;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    @JsonIgnore
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "workplace")
    private String workplace;
    @Column(name = "region")
    private String region;
    @Column(name = "is_admin")
    private boolean admin;

    public User() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return admin == user.admin
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password)
                && Objects.equals(email, user.email)
                && Objects.equals(workplace, user.workplace)
                && Objects.equals(region, user.region);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }

    public interface AllJsonView {

    }

    public interface NoPwdJsonView {

    }
}

