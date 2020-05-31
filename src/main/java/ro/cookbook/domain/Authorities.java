package ro.cookbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authorities extends BaseEntity<Long> implements GrantedAuthority {

    private String authority;
    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return authority;
    }
}
