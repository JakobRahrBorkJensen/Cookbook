package jrbj.training.cookbook.recipe;

import jrbj.training.cookbook.ingredient.IngredientService;
import jrbj.training.cookbook.ingredient.data.IngredientEntity;
import jrbj.training.cookbook.ingredient.dto.IngredientDTO;
import jrbj.training.cookbook.ingredient.dto.MeasurementUnit;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.data.RecipeRepository;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import jrbj.training.cookbook.recipe.dto.RecipeTitleDTO;
import jrbj.training.cookbook.recipe.search_strategy.SearchStrategies;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    IngredientService ingredientService;

    @InjectMocks
    RecipeService recipeService;

    @Test
    void getRecipesContainingIngredients_whenAnyStrategy_thenReturnAnyMatch() {
        // Given
        //initServiceAndInvertedIndex();
        var strategy = SearchStrategies.ANY;
        List<String> ingredients = new ArrayList<>(Arrays.asList("name", "invalid"));
        var pageable = PageRequest.of(0, 5);

        doReturn(validSampleIngredientEntity()).when(ingredientService).mapDtoToEntity(any());
        doReturn(validSampleRecipeEntity()).when(recipeRepository).save(any());
        doReturn(validSampleRecipeTitleDto()).when(recipeRepository).findByIds(any(), any());

        var recipe1 = recipeService.createRecipe(validSampleRecipeDTO());

        // When
        var result = recipeService.getRecipesContainingIngredients(ingredients, strategy, pageable);

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("title");
    }

    private IngredientEntity validSampleIngredientEntity() {
        return new IngredientEntity(1L, "name", 2.5, MeasurementUnit.STK, null);
    }

    private void initServiceAndInvertedIndex() {
        doReturn(validSampleRecipeEntity()).when(recipeRepository.save(any()));
        var set = new HashSet<Long>();
        set.add(1L);
        when(recipeRepository.findByIds(any(), any())).thenReturn(validSampleRecipeTitleDto());

        var recipe1 = recipeService.createRecipe(validSampleRecipeDTO());
    }

    /**
     * Creates a valid sample recipe
     */
    private RecipeEntity validSampleRecipeEntity() {
        var recipe = new RecipeEntity(
                1L,
                "title",
                "summary",
                "description",
                new ArrayList<>(),
                5,
                2,
                "link");
        var ingredient = new IngredientEntity(1L, "name", 2.5, MeasurementUnit.STK, null);
        recipe.addIngredient(ingredient);
        return recipe;
    }

    private RecipeDTO validSampleRecipeDTO() {
        List<IngredientDTO> ingredients = new ArrayList<>();
        ingredients.add(IngredientDTO.builder()
                .name("name")
                .amount(2.5)
                .unit(MeasurementUnit.STK)
                .build());

        return RecipeDTO.builder()
                .title("title")
                .summary("summary")
                .description("description")
                .ingredients(ingredients)
                .estimatedTime(5)
                .people(2)
                .link("link")
                .build();
    }

    private Page<RecipeTitleDTO> validSampleRecipeTitleDto() {
        List<RecipeTitleDTO> list = new ArrayList<>();
        var dto = new RecipeTitleDTO() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getTitle() {
                return "title";
            }
        };
        list.add(dto);
        return new PageImpl<>(list);
    }

}