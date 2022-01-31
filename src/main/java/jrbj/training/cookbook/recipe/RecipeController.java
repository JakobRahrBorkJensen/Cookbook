package jrbj.training.cookbook.recipe;

import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@AllArgsConstructor
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping(path = "/recipes", produces = "application/json")
    public ResponseEntity<RecipeEntity> createRecipe(@Valid @RequestBody RecipeDTO recipeDto) {
        var response = recipeService.createRecipe(recipeDto);
        return new ResponseEntity<>(response, CREATED);
    }

    // GET MAPPING FOR RECIPES CONTAINING INGREDIENTS AS LIST
    // LIST ONLY CONTAINS TITLES
    // CAN HATEAOS BE USED TO CREATE LINKS FOR INDIVIDUAL RECIPES (FULL)?

}
