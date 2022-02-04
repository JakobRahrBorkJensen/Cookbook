package jrbj.training.cookbook.recipe;

import jrbj.training.cookbook.exception.HttpClientException;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import jrbj.training.cookbook.recipe.dto.RecipeTitleDTO;
import jrbj.training.cookbook.recipe.search_strategy.SearchStrategies;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@AllArgsConstructor
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * Create new recipes with ingredients.
     */
    @PostMapping(path = "/recipes", produces = "application/json")
    @ResponseStatus(CREATED)
    public RecipeEntity createRecipe(@Valid @RequestBody RecipeDTO recipeDto) {
        return recipeService.createRecipe(recipeDto);
    }

    /**
     * Get list of recipes that include specified ingredients.
     * @param ingredientNames Names of ingredients, separated by comma.
     *                        If no ingredients are listed, all recipes will be returned
     * @param searchStrategyString Chosen search strategy. See options in {@link SearchStrategies}.
     *                             By default this is set to "all".
     * @param pageable Pageable object allowing retrieving only one page of recipes at a time.
     * @return Titles and IDs of matching list of recipes.
     */
    @GetMapping(path = "/recipes", produces = "application/json")
    public List<RecipeTitleDTO> getRecipesContainingIngredients(
            @RequestParam(name = "ingredients", required = false, defaultValue = "") List<String> ingredientNames,
            @RequestParam(name = "search_strategy", required = false, defaultValue = "all") String searchStrategyString,
            Pageable pageable) {

        // If no ingredients are named, set strategy to none, as this strategy will then provide all recipes.
        if (ingredientNames.isEmpty()) {
            searchStrategyString = SearchStrategies.NONE.getName();
        }

        var searchStrategy = verifySearchStrategy(searchStrategyString);

        return recipeService.getRecipesContainingIngredients(
                ingredientNames, searchStrategy, pageable);
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
