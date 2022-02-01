package jrbj.training.cookbook.recipe.search_strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains the ALL search strategy, meaning each matching recipe must contain all of the
 * ingredients (identified by name)
 */
public class AllSearchStrategy implements SearchStrategy {
    @Override
    public Set<Long> search(Map<String, Set<Long>> invertedIndex, List<String> ingredientNames) {
        // Add initial set for first ingredient
        var setOfRecipes = ingredientNames.size() > 0 ? invertedIndex.get(ingredientNames.get(0)) : null;

        if (null == setOfRecipes) {
            return new HashSet<>();
        }

        // Go through rest of ingredients, and only retain recipes that contains all ingredients
        if (ingredientNames.size() > 1) {
            for (int i = 1; i < ingredientNames.size(); i++) {
                var matchingRecipes = invertedIndex.get(ingredientNames.get(i));
                setOfRecipes.retainAll(matchingRecipes);
            }
        }

        return setOfRecipes;
    }
}
