package jrbj.training.cookbook.recipe.dto;

import jrbj.training.cookbook.ingredient.dto.IngredientDTO;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Main DTO object for recipes only leaving out the database ID.
 * Related ingredients are represented as ingredient DTO objects instead of entity objects.
 */
@Value
@Builder
@Jacksonized
public class RecipeDTO {

    @NotEmpty(message = "[title] Title of recipe should be set")
    String title;

    String summary;

    @NotEmpty(message = "[description] Description of recipe should be set")
    String description;

    @Valid
    List<IngredientDTO> ingredients;

    @Min(value = 0, message = "[estimatedTime] Estimated time of completion should be a positive integer")
    Integer estimatedTime;

    @Min(value = 1, message = "[people] Course should be for at least 1 person")
    Integer people;

    String link;
}
