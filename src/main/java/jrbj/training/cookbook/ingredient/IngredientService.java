package jrbj.training.cookbook.ingredient;

import jrbj.training.cookbook.ingredient.dto.IngredientDTO;
import jrbj.training.cookbook.ingredient.data.IngredientEntity;
import jrbj.training.cookbook.ingredient.data.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientEntity mapDtoToEntity(IngredientDTO dto) {
        // TODO try out MapStruct
        var ingredient = new IngredientEntity();
        ingredient.setName(dto.getName());
        ingredient.setAmount(dto.getAmount());
        ingredient.setUnit(dto.getUnit());
        return ingredient;
    }
}
