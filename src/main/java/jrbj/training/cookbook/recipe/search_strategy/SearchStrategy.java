package jrbj.training.cookbook.recipe.search_strategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Strategy definition for search strategy implemented according to Strategy Pattern
 */
public interface SearchStrategy {
    /**
     * Search through the provided inverted index of ingredients to identify matching recipe (IDs).
     * Meaning of "matching" depends on chosen strategy.
     * @param invertedIndex Populated inverted index having ingredient names as key and List of recipe IDs as values.
     * @param ingredientNames Names of ingredients that is search for.
     * @return Set of recipe IDs matching the search strategy.
     */
    Set<Long> search(Map<String, Set<Long>> invertedIndex,
                                List<String> ingredientNames);
}
