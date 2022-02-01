package jrbj.training.cookbook.recipe.search_strategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Container for the SearchStrategy object. Allows for setting the desired strategy and executing it.
 */
public class SearchStrategyContainer {
    private SearchStrategy searchStrategy;

    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    /**
     * Execute search.
     * See description of parameters in {@link SearchStrategy}.
     */
    public Set<Long> performSearch(Map<String, Set<Long>> invertedIndex,
                                              List<String> ingredientNames) {
        return searchStrategy.search(invertedIndex, ingredientNames);
    }
}
