package ro.cookbook.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.cookbook.domain.Recipe;
import ro.cookbook.domain.User;
import ro.cookbook.repositories.RecipeRepository;
import ro.cookbook.util.PdfExportHelper;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ro.cookbook.util.Utils.IMAGES_ROOT_PATH;
import static ro.cookbook.util.Utils.contains;
import static ro.cookbook.util.Utils.containsIgnoreCase;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repository;
    @Autowired
    private PdfExportHelper pdfExportHelper;

    public List<Recipe> getRecipes() {
        return repository.findAll();
    }

    public List<Recipe> getMyFavourites(User user) {
        return repository.findAll().stream()
                .filter(recipe -> contains(recipe.getLikedBy(), user))
                .collect(Collectors.toList());
    }

    public List<Recipe> getMyRecipes(User user) {
        return repository.findAll().stream()
                .filter(recipe -> recipe.getAddedBy().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public void addToFavourites(String recipeId, User user) {
        Recipe recipe = repository.getOne(Long.valueOf(recipeId));
        Set<User> users = recipe.getLikedBy();
        users.add(user);
        recipe.setLikedBy(users);
        repository.save(recipe);
    }

    public ByteArrayInputStream exportToPdf(String recipeId, User user) {
        Recipe recipe = repository.getOne(Long.valueOf(recipeId));
        return pdfExportHelper.createPdf(recipe, user);
    }

    public List<Recipe> filterByName(String name) {

        return repository.findAll().stream()
                .filter(r -> containsIgnoreCase(r.getRecipeName(), (name)))
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
        Path path = Paths.get(IMAGES_ROOT_PATH + recipe.getId() + ".jpg");
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
