package jrbj.training.cookbook.recipe;

import jrbj.training.cookbook.ingredient.IngredientService;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.data.RecipeRepository;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import jrbj.training.cookbook.recipe.dto.RecipeTitleDTO;
import jrbj.training.cookbook.recipe.search_strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for all logic related to recipes.
 */
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;

    private final Map<String, Set<Long>> invertedIndex = new HashMap<>();

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
    }

    /**
     * Create and persist recipe, including its ingredients.
     * Ensures recipe and related ingredients are kept in sync.
     *
     * All ingredients are added to the inverted index
     */
    public RecipeEntity createRecipe(RecipeDTO recipeToCreate) {
        var recipe = new RecipeEntity();
        recipe.setTitle(recipeToCreate.getTitle());
        recipe.setSummary(recipeToCreate.getSummary());
        recipe.setDescription(recipeToCreate.getDescription());
        recipe.setEstimatedTime(recipeToCreate.getEstimatedTime());
        recipe.setPeople(recipeToCreate.getPeople());
        recipe.setLink(recipeToCreate.getLink());

        recipeToCreate.getIngredients().stream()
                .map(ingredientService::mapDtoToEntity)
                .forEach(recipe::addIngredient);

        var response =  recipeRepository.save(recipe);

        // Add ingredients to inverted index
        response.getIngredients().forEach(ingredient ->
                addToInvertedIndex(ingredient.getName(), response.getId()));

        return response;
    }

    /**
     * Add ingredient to inverted index.
     * If ingredient is already in the inverted index, the new recipe ID is simply added to the set of matching recipes.
     * @param ingredientName Name of ingredient to be uses as key in the inverted index
     * @param recipeId ID of recipe to be added to list of matching recipes as values in the inverted index.
     */
    private void addToInvertedIndex(String ingredientName, Long recipeId) {
        ingredientName = ingredientName.toLowerCase();
        var setOfLines = invertedIndex.getOrDefault(ingredientName, new HashSet<>());
        setOfLines.add(recipeId);
        invertedIndex.put(ingredientName, setOfLines);
    }

    /**
     * Get eagerly loaded recipe with its related ingredients loaded.
     * @param recipeId ID of recipe to be fetched.
     */
    public RecipeEntity getFullRecipe(Long recipeId) {
        return recipeRepository.findByIdAndFetchIngredientsEagerly(recipeId)
                .orElseThrow(() -> new RuntimeException("No recipe found by that ID"));
    }

    /**
     * Get list of recipes that matches desired ingredients and strategy
     * @param ingredientNames List of ingredients (names)
     * @param searchStrategy Strategy to identify matching recipes
     * @param pageable Pageable object to allow for only retrieving matching recipes page by page.
     * @return Titles and IDs of list of matching recipes.
     */
    public List<RecipeTitleDTO> getRecipesContainingIngredients(
            List<String> ingredientNames,
            SearchStrategies searchStrategy,
            Pageable pageable) {

        // Init search strategy container and make ingredientNames lowercase
        SearchStrategyContainer chosenSearchStrategy = new SearchStrategyContainer();
        ingredientNames = ingredientNames.stream().map(String::toLowerCase).collect(Collectors.toList());

        // Set chosen search strategy
        chosenSearchStrategy.setSearchStrategy(initSearchStrategy(searchStrategy));

        // Perform search
        var recipeIdSet = chosenSearchStrategy.performSearch(invertedIndex, ingredientNames);

        // Get RecipeDTOs
        var paginatedRecipes =  getRecipeTitles(recipeIdSet, pageable);
        return paginatedRecipes.getContent();
    }

    /**
     * Get an instance of the desired SearchStrategy.
     */
    private SearchStrategy initSearchStrategy(SearchStrategies searchStrategy) {
        switch (searchStrategy) {
            case ANY:
                return new AnySearchStrategy();
            case ALL:
                return new AllSearchStrategy();
            case NONE:
                return new NoneSearchStrategy(recipeRepository.count());
            default:
                throw new RuntimeException("Unknown search strategy");
        }
    }

    /**
     * Fetch list of recipes matching specified recipe IDs.
     * @return Titles and IDs of matching recipes.
     */
    private Page<RecipeTitleDTO> getRecipeTitles(Set<Long> recipeIds, Pageable pageable) {
        return recipeRepository.findByIds(recipeIds, pageable);
    }


}
