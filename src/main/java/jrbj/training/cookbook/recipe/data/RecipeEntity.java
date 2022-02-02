package jrbj.training.cookbook.recipe.data;

import jrbj.training.cookbook.ingredient.data.IngredientEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database entity for recipes. Has a (bidirectional) one-to-many relation to ingredients,
 * this not being the owning side.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "description", columnDefinition = "CLOB", nullable = false)
    private String description;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Column(name = "ingredients")
    private List<IngredientEntity> ingredients = new ArrayList<>();

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Column(name = "people")
    private Integer people;

    @Column(name = "link")
    private String link;

    /**
     * Add ingredient to the list prior to persisting. Always use this method when creating an ingredient related to
     * a recipe instead of referring directly to the recipe. By using this method, both objects are kept in sync.
     */
    public void addIngredient(IngredientEntity ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    /**
     * Remove ingredient from the list prior to persisting. Always use this method when deleting an ingredient related
     * to a recipe instead of referring directly to the recipe. By using this method, both objects are kept in sync.
     */
    public void removeIngredient(IngredientEntity ingredient) {
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
    }
}
