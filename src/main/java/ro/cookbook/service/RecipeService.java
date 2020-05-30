package ro.cookbook.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.cookbook.domain.Recipe;
import ro.cookbook.repositories.RecipeRepository;

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

    public List<Recipe> filterByName(final String name) {

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

    public Recipe add(Recipe recipe) {
        recipe.setTags(recipe.getTags().toLowerCase());
        repository.save(recipe);
        return recipe;
    }

    public Recipe getById(Long id){
        Optional<Recipe> recipe=repository.findById(id);
        return recipe.orElse(null);
    }
//
//    public Recipe add(String recipe, MultipartFile file) {
//
//        Gson g = new Gson();
//        Recipe newRecipe = g.fromJson(recipe, Recipe.class);
////
////        Image image = new Image();
////        try {
////            image.setImageName(file.getOriginalFilename());
////            image.setImage(file.getBytes());
////        } catch (IOException e) {
////            System.out.println("Image was built");
////        }
//
////        Set<Image> images = files.stream().map(f -> {
////            Image image = new Image();
////            try {
////                image.setImageName(f.getOriginalFilename());
////                image.setImage(f.getBytes());
////            } catch (IOException e) {
////                System.out.println(e.getCause());
////            }
////            return image;
////       }).collect(Collectors.toSet());
//
//     //   newRecipe.setImage(image);
//        repository.save(newRecipe);
//        return newRecipe;
//    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
