package ro.cookbook.domain;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity<Long> {

    private String firstName;
    private String lastName;
   // @UniqueElements
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities = new HashSet<>();

}
