package ro.cookbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.cookbook.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
