package jrbj.training.cookbook.recipe.search_strategy;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class AnySearchStrategyTest {

    /* Tests that recipe containing bacon is returned, although it does not contain apple */
    @Test
    void whenOnlySomeIngredientsAreIncluded_thenStillReturnTheseRecipes() {
        // Given
        var strategy = new AnySearchStrategy();
        Map<String, Set<Long>> invertedIndex = new HashMap<>();
        Long recipeId = 4L;
        invertedIndex.put("bacon", new HashSet<>(Collections.singletonList(recipeId)));
        List<String> ingredients = new ArrayList<>(Arrays.asList("apple", "bacon"));

        // When
        var result = strategy.search(invertedIndex, ingredients);

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).contains(recipeId);
    }

    /* Tests that when no recipes contain specified ingredients, then an empty set is returned, not null */
    @Test
    void whenNoIngredientsAreIncluded_thenReturnsEmptySet() {
        // Given
        var strategy = new AnySearchStrategy();
        Map<String, Set<Long>> invertedIndex = new HashMap<>();
        Long recipeId = 4L;
        invertedIndex.put("bacon", new HashSet<>(Collections.singletonList(recipeId)));
        List<String> ingredients = new ArrayList<>(Arrays.asList("apple", "banana"));

        // When
        var result = strategy.search(invertedIndex, ingredients);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    /* Tests that ingredients are part of multiple recipes, then all these recipes is returned */
    @Test
    void whenIngredientsAreIncludedInMultipleRecipes_thenReturnsAllRelevantRecipes() {
        // Given
        var strategy = new AnySearchStrategy();
        Map<String, Set<Long>> invertedIndex = new HashMap<>();
        Set<Long> baconRecipeId = new HashSet<>(Arrays.asList(1L, 2L));
        invertedIndex.put("bacon", new HashSet<>(baconRecipeId));
        List<String> ingredients = new ArrayList<>(Collections.singletonList("bacon"));

        // When
        var result = strategy.search(invertedIndex, ingredients);

        // Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(1L);
        assertThat(result).contains(2L);
    }

    /* Tests that when multiple ingredients are part of same recipes, then all these recipes is returned only once */
    @Test
    void whenMultipleIngredientsAreIncludedInMultipleRecipes_thenReturnsAllRelevantRecipesOnce() {
        // Given
        var strategy = new AnySearchStrategy();
        Map<String, Set<Long>> invertedIndex = new HashMap<>();
        Set<Long> baconRecipeId = new HashSet<>(Arrays.asList(1L, 2L));
        Set<Long> eggRecipeId = new HashSet<>(Arrays.asList(2L, 3L));
        invertedIndex.put("bacon", new HashSet<>(baconRecipeId));
        invertedIndex.put("egg", new HashSet<>(eggRecipeId));
        List<String> ingredients = new ArrayList<>(Arrays.asList("bacon", "egg"));

        // When
        var result = strategy.search(invertedIndex, ingredients);

        // Then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(1L);
        assertThat(result).contains(2L);
        assertThat(result).contains(3L);
    }
}