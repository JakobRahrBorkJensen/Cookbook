package jrbj.training.cookbook.recipe;

import jrbj.training.cookbook.ingredient.IngredientService;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.data.RecipeRepository;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        return response;
    }

    public RecipeEntity getFullRecipe(Long recipeId) {
        return recipeRepository.findByIdAndFetchIngredientsEagerly(recipeId)
                .orElseThrow(() -> new RuntimeException("No recipe found by that ID"));
    }


}
