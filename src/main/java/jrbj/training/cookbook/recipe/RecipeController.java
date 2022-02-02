package jrbj.training.cookbook.recipe;

import jrbj.training.cookbook.exception.HttpClientException;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import jrbj.training.cookbook.recipe.dto.RecipeTitleDTO;
import jrbj.training.cookbook.recipe.search_strategy.SearchStrategies;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@AllArgsConstructor
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Create new recipes with ingredients.
     */
    @PostMapping(path = "/recipes", produces = "application/json")
    public ResponseEntity<RecipeEntity> createRecipe(@Valid @RequestBody RecipeDTO recipeDto) {
        var response = recipeService.createRecipe(recipeDto);
        return new ResponseEntity<>(response, CREATED);
    }

    /**
     * Get list of recipes that include specified ingredients.
     * @param ingredientNames Names of ingredients, separated by comma.
     * @param searchStrategyString Chosen search strategy. See options in {@link SearchStrategies}.
     * @param pageable Pageable object allowing retrieving only one page of recipes at a time.
     * @return Titles and IDs of matching list of recipes.
     */
    @GetMapping(path = "/recipes/contains", produces = "application/json")
    public ResponseEntity<List<RecipeTitleDTO>> getRecipesContainingIngredients(
            @RequestParam(name = "ingredients") List<String> ingredientNames,
            @RequestParam(name = "search_strategy") String searchStrategyString,
            Pageable pageable) {

        var searchStrategy = verifySearchStrategy(searchStrategyString);

        var recipeList = recipeService.getRecipesContainingIngredients(
                ingredientNames, searchStrategy, pageable);

        return new ResponseEntity<>(recipeList, OK);
    }

    /**
     * Verify that provided search strategy is a valid option.
     * @param searchStrategyString Provided search strategy through API call.
     * @return SearchStrategies enum value matching the provided option.
     * @throws HttpClientException if provided search strategy is not recognised.
     */
    private SearchStrategies verifySearchStrategy(String searchStrategyString) {
        var strategy = SearchStrategies.get(searchStrategyString);
        if (strategy == null) {
            throw new HttpClientException(
                    "invalid-arguments",
                    "Provided search strategy unknown. Allowed strategies: %s",
                    Arrays.toString(SearchStrategies.values()));
        }
        return strategy;
    }
}
