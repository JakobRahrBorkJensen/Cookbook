package jrbj.training.cookbook.ingredient.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jrbj.training.cookbook.ingredient.dto.MeasurementUnit;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import lombok.*;

import javax.persistence.*;

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

    @Column(name = "name")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientEntity)) return false;
        return id != null && id.equals(((IngredientEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
