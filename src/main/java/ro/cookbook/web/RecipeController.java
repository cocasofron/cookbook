package ro.cookbook.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import java.io.ByteArrayInputStream;
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
    public String getRecipes(@AuthenticationPrincipal User user, Model model) {
        List<Recipe> recipes = service.getRecipes();
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "recipes/recipes";
    }

    @GetMapping("recipes/myFavourites")
    public String getMyFavourites(@AuthenticationPrincipal User user, Model model) {
        List<Recipe> recipes = service.getMyFavourites(user);
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "recipes/myFavourites";
    }

    @PostMapping("/recipes/addToFavourites")
    public String addToFavourites(@AuthenticationPrincipal User user, String recipeId, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("recipeId", recipeId);
        service.addToFavourites(recipeId, user);
        return "success";
    }

    @PostMapping(value = "/exportToPdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> exportToPdf(@AuthenticationPrincipal User user, String recipeId, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("recipeId", recipeId);

        ByteArrayInputStream bis = service.exportToPdf(recipeId, user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=recipe.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    @GetMapping("recipes/myRecipes")
    public String getMyRecipes(@AuthenticationPrincipal User user, Model model) {
        List<Recipe> recipes = service.getMyRecipes(user);
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "recipes/myRecipes";
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
        recipe.setAddedBy(user);
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

    @GetMapping("/deleteRecipe/{recipeId}")
    String deleteById(@AuthenticationPrincipal User user, Model model, @PathVariable Long recipeId) {
        model.addAttribute("user", user);
        service.deleteById(recipeId);
        return "success";
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/recipes/deleteAll", method = RequestMethod.GET)
    ResponseEntity<?> deleteAll() {
        LOG.warn("DELETING all recipes");
        service.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}