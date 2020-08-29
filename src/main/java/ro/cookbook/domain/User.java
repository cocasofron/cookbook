package ro.cookbook.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<Long> {

    private String firstName;
    private String lastName;
    // @UniqueElements
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities = new HashSet<>();
    @OneToMany
    private Set<Recipe> addedRecipes;
    @ManyToMany
    private Set<Recipe> favouriteRecipes;

    @Override
    public String toString() {
        return firstName;
    }
}
