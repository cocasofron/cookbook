package ro.cookbook.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "recipe")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Recipe extends BaseEntity<Long> {

    private String recipeName;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
    private String cookingMethod;
    private String tags;
    @ManyToOne
    private User addedBy;
    @ManyToMany
    private Set<User> likedBy;

    @Override
    public Long getId() {
        return super.getId();
    }
}
