package jrbj.training.cookbook.ingredient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;

/**
 * Main DTO object for ingredients only leaving out the database ID.
 * Related recipe is represented as recipe DTO object instead of entity object.
 */
@Value
@Builder
@Jacksonized
public class IngredientDTO {
    @NotEmpty(message = "[name] Name of ingredient should be set")
    String name;

    Double amount;

    MeasurementUnit unit;

    @JsonIgnoreProperties
    RecipeDTO recipe;
}
