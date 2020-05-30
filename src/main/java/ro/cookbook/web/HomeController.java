package ro.cookbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ro.cookbook.domain.Recipe;
import ro.cookbook.domain.User;
import ro.cookbook.repositories.RecipeRepository;
import ro.cookbook.service.RecipeService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeService recipeService;

    @GetMapping("/")
    public String getHome(@AuthenticationPrincipal User user, Model model) {
        List<Recipe> tastyRecipes = recipeService.filterByCategory(Arrays.asList("pizza", "pasta"));
        List<Recipe> healthyRecipes = recipeService.filterByCategory(Arrays.asList("salads", "healthy"));
        List<Recipe> desserts = recipeService.filterByCategory(Collections.singletonList("desserts"));
        model.addAttribute("tastyRecipes", tastyRecipes);
        model.addAttribute("healthyRecipes", healthyRecipes);
        model.addAttribute("desserts", desserts);
        model.addAttribute("user", user);
        return "home/home";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error";
    }
}
