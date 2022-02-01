package jrbj.training.cookbook.recipe.search_strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains the NONE search strategy, meaning each matching recipe must contain NONE of the ingredients.
 * As the design of the strategy (see {@link SearchStrategy} expects to receive matching recipes,
 * this strategy can only point out the ones that contains ingredients, not the ones that don't.
 * To overcome this, the number of recipes is passed in the constructor, allowing a full set of recipes (IDs) to be
 * constructed, from which the ANY strategy can be used to collect a set of recipes, that should be removed from
 * the full set.
 */
public class NoneSearchStrategy implements SearchStrategy {
    private Set<Long> setOfAllRecipes;

    public NoneSearchStrategy(long numberOfRecipesInRepo) {
        initSetOfAllRecipes(numberOfRecipesInRepo);
    }

    private void initSetOfAllRecipes(long numberOfRecipesInRepo) {
        setOfAllRecipes = new HashSet<>();
        for (long i = 1; i <= numberOfRecipesInRepo; i++) {
            setOfAllRecipes.add(i);
        }
    }

    @Override
    public Set<Long> search(Map<String, Set<Long>> invertedIndex, List<String> ingredientNames) {
        // Retrieve all "contaminated" recipes by using the ANY strategy
        var contaminatedRecipes = new AnySearchStrategy().search(invertedIndex, ingredientNames);
        // Remove them from set of all recipes
        setOfAllRecipes.removeAll(contaminatedRecipes);
        return setOfAllRecipes;
    }
}
