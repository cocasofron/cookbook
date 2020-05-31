package ro.cookbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
        return "recipes/recipes";
    }

    @GetMapping("/recipes/new")
    public String displayForm(@AuthenticationPrincipal User user, Model model) {
        Recipe aRecipe = new Recipe();
        model.addAttribute("recipe", aRecipe);
        model.addAttribute("user", user);
        return "recipes/addRecipe";
    }

    @PostMapping("/recipes/addRecipe")
    public String addRecipe(@AuthenticationPrincipal User user, @ModelAttribute Recipe recipe,
                            @RequestParam(required = false) MultipartFile file,
                            Model model) {
        model.addAttribute("user", user);
        List<Ingredient> ingredients = recipe.getIngredients().stream()
                .map(ingredient -> new Ingredient(ingredient.getIngredient(), ingredient.getQuantity(), recipe))
                .collect(Collectors.toList());
        recipe.setIngredients(ingredients);
        try {
            service.add(recipe, file);
        } catch (Exception e) {
            throw new RuntimeException("The recipe/images were not saved!");
        }
        return "success";
    }

    @PostMapping("/searchRecipes")
    public String searchEverything(@AuthenticationPrincipal User user, Model model, String search) {
        model.addAttribute("search", search);
        Set<Recipe> recipes = new HashSet<>();
        recipes.addAll(service.filterByName(search));
        List<String> keywordsList = Arrays.asList(search.split(" "));
        recipes.addAll(service.filterByIngredients(keywordsList));
        recipes.addAll(service.filterByCategory(keywordsList));
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "recipes/searchResult";
    }

    @GetMapping("/recipe/{recipeId}")
    String getById(@AuthenticationPrincipal User user, Model model, @PathVariable Long recipeId) {
        Recipe recipe = service.getById(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("user", user);
        return "recipes/recipe";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/recipes/deleteAll", method = RequestMethod.GET)
    ResponseEntity<?> deleteAll() {
        LOG.warn("DELETING all recipes");
        service.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}