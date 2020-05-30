package ro.cookbook.domain;

import lombok.*;
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
}
