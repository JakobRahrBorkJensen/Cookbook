package jrbj.training.cookbook.recipe.search_strategy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains the ANY search strategy, meaning each matching recipe should just contain any one (or multiple)
 * of the ingredients (identified by name)
 */
public class AnySearchStrategy implements SearchStrategy {
    @Override
    public Set<Long> search(Map<String, Set<Long>> invertedIndex, List<String> ingredientNames) {
        return ingredientNames.stream()
                .map(invertedIndex::get)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
