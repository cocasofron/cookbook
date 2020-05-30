package ro.cookbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.cookbook.domain.Ingredient;
import ro.cookbook.domain.Recipe;
import ro.cookbook.domain.User;
import ro.cookbook.service.RecipeService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class RecipeController {
    private static final Logger LOG =
            LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private RecipeService service;

    @GetMapping("/getRecipes")
    String getRecipes(@AuthenticationPrincipal User user, Model model) {
        List<Recipe> recipes = service.getRecipes();
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "recipes";
    }

    @RequestMapping(value = "/recipes/search", method = RequestMethod.GET)
    List<Recipe> getRecipe(@RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "ingredients") List<String> ingredientsList, @RequestParam(required = false, value = "tags") List<String> tags) {
        LOG.info("Retrieving recipes");
        if (name != null) {
            return service.filterByName(name);
        } else if (ingredientsList != null) {
            return service.filterByIngredients(ingredientsList);
        } else if (tags != null) {
            return service.filterByCategory(tags);
        }
        return null;
    }

    @GetMapping("/recipes/new")
    public String displayForm(@AuthenticationPrincipal User user, Model model) {
        Recipe aRecipe = new Recipe();
        model.addAttribute("recipe", aRecipe);
        model.addAttribute("user", user);
        return "addRecipe";
    }

    @PostMapping("/recipes/addRecipe")
    public String addRecipe(@AuthenticationPrincipal User user, Recipe recipe, Model model) {
        model.addAttribute("user", user);
        List<Ingredient> ingredients = recipe.getIngredients().stream().map(ingredient -> new Ingredient(ingredient.getIngredient(), ingredient.getQuantity(), recipe)).collect(Collectors.toList());
        recipe.setIngredients(ingredients);
        service.add(recipe);
        return "success";
    }

    @PostMapping("/searchRecipes")
    public String searchEverything(@AuthenticationPrincipal User user, Model model, String search) {
        model.addAttribute("search", search);
        Set<Recipe> recipes = new HashSet<>();
        service.filterByName(search);
        List<String> keywordsList = Arrays.asList(search.split(" "));
        recipes.addAll(service.filterByIngredients(keywordsList));
        recipes.addAll(service.filterByCategory(keywordsList));
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "recipes/searchResult";
    }

//    @RequestMapping(value = "/recipes/addRecipe", method = RequestMethod.POST, consumes = {"multipart/form-data"})
//    Recipe addRecipe(@ModelAttribute(name = "recipe") String recipe, @RequestParam(required = false, name = "file") MultipartFile files) {
//        LOG.info("Adding a new recipe");
//
//        return service.add(recipe, files);
//
//    }

//    @RequestMapping(value = "/recipes/{recipeId}", method =
//            RequestMethod.DELETE)
//    ResponseEntity<?> deleteRecipe(@PathVariable Long recipeId) {
//        LOG.info("deleteRecipe: recipeId={}", recipeId);
//
//        service.deleteById(recipeId);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("/recipe/{recipeId}")
    String getById(@AuthenticationPrincipal User user, Model model, @PathVariable Long recipeId) {
        Recipe recipe = service.getById(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("user", user);
        return "recipe";
    }

    @RequestMapping(value = "/recipes/deleteAll", method =
            RequestMethod.DELETE)
    ResponseEntity<?> deleteAll() {
        LOG.warn("DELETING all recipes");

        service.deleteAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }
}