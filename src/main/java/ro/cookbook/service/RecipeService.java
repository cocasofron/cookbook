package ro.cookbook.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.cookbook.domain.Recipe;
import ro.cookbook.repositories.RecipeRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repository;

    public List<Recipe> getRecipes() {
        return repository.findAll();
    }

    public List<Recipe> filterByName(String name) {

        return repository.findAll().stream()
                .filter(r -> r.getRecipeName().equalsIgnoreCase(name) || r.getRecipeName().contains(name))
                .collect(Collectors.toList());
    }

    public List<Recipe> filterByIngredients(List<String> ingredients) {
        List<Recipe> recipes = new ArrayList<>();

        for (String ingredient : ingredients) {
            recipes.addAll(repository.findAll().stream()
                    .filter(r -> r.getIngredients().toString().contains(ingredient))
                    .collect(Collectors.toSet()));
        }
        return recipes;
    }

    public List<Recipe> filterByCategory(List<String> tags) {
        List<Recipe> recipes = new ArrayList<>();

        for (String tag : tags) {
            recipes.addAll(repository.findAll().stream()
                    .filter(r -> r.getTags().contains(tag))
                    .collect(Collectors.toList()));
        }
        return recipes;
    }

    public Recipe add(Recipe recipe, MultipartFile file) throws Exception {
        recipe.setTags(recipe.getTags().toLowerCase());
        repository.save(recipe);
        Path path = Paths.get("src/main/resources/static/images/" + recipe.getId() + ".jpg");
        Files.write(path, file.getBytes());
        return recipe;
    }

    public Recipe getById(Long id) {
        Optional<Recipe> recipe = repository.findById(id);
        return recipe.orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
