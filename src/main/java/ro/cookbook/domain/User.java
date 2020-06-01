package ro.cookbook.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<Long> {

    private String firstName;
    private String lastName;
    // @UniqueElements
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Authorities> authorities = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Recipe> addedRecipes;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Recipe> myFavourites;


    @Override
    public String toString() {
        return firstName;
    }
}
