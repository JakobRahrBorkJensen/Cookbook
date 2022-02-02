package jrbj.training.cookbook.ingredient.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jrbj.training.cookbook.ingredient.dto.MeasurementUnit;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import lombok.*;

import javax.persistence.*;

/**
 * Database entity for ingredients. Has a (bidirectional) many-to-one relation to recipes, this being the owning side.
 */
@Getter
@Setter
@ToString(exclude = { "recipe" })
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class IngredientEntity {

    @Id
    @GeneratedValue()
    @Column(name = "ingredient_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "unit")
    @Enumerated
    private MeasurementUnit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private RecipeEntity recipe;

    /**
     * Equal method overridden to compare only by ID, as objects can be in a changed state when being compared
     * to another instance of itself.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientEntity)) return false;
        return id != null && id.equals(((IngredientEntity) o).getId());
    }

    /**
     * Hash method overridden to ensure it is constant for the class. This ensures valid equality checks if ID is null.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
