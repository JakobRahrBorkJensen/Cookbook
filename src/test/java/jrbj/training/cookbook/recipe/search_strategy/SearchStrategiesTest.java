package jrbj.training.cookbook.recipe.search_strategy;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SearchStrategiesTest {

    /**
     * Arguments for testing case-insensitivity of get method
     *
     * @return Stream of arguments
     */
    private static Stream<Arguments> nameArguments() {
        return Stream.of(
                Arguments.of("ANY"),
                Arguments.of("any"),
                Arguments.of("Any"),
                Arguments.of("aNy")
        );
    }

    /**
     * Tests that get method is case-insensitve
     */
    @ParameterizedTest
    @MethodSource("nameArguments")
    void whenProvidedStringMatchesNameAttribute_thenReturnEnumValueNoMatterCase(String name) {
        // When
        var searchStrategy = SearchStrategies.get(name);

        // Then
        assertThat(searchStrategy).isNotNull();
        assertThat(searchStrategy).isEqualTo(SearchStrategies.ANY);
    }

    /* Tests that null is returned if value is not known */
    @Test
    void whenProvidedStringIsNotAValidOption_thenReturnsNull() {
        // Given
        var option = "invalid";

        // When
        var searchStrategy = SearchStrategies.get(option);

        // Then
        assertThat(searchStrategy).isNull();
    }
}