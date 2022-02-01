package jrbj.training.cookbook.ingredient;

import jrbj.training.cookbook.ingredient.data.IngredientEntity;
import jrbj.training.cookbook.ingredient.dto.IngredientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for all logic related to ingredients.
 */
@Service
public class IngredientService {

    @Autowired
    public IngredientService() {}

    /**
     * Maps an ingredient DTO into its corresponding entity
     */
    public IngredientEntity mapDtoToEntity(IngredientDTO dto) {
        // TODO try out MapStruct
        var ingredient = new IngredientEntity();
        ingredient.setName(dto.getName());
        ingredient.setAmount(dto.getAmount());
        ingredient.setUnit(dto.getUnit());
        return ingredient;
    }


}
