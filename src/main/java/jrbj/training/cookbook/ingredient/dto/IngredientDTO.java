package jrbj.training.cookbook.ingredient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class IngredientDTO {
    String name;

    Double amount;

    MeasurementUnit unit;

    @JsonIgnoreProperties
    RecipeDTO recipe;
}
