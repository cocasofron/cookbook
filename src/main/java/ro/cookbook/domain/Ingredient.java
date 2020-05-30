package ro.cookbook.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Ingredient extends BaseEntity<Long> {

    private String ingredient;
    private String quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    private Recipe recipe;

    @Override
    public String toString() {
        return ingredient + ':' + quantity;
    }
}
