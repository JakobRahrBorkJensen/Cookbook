package jrbj.training.cookbook;

import jrbj.training.cookbook.recipe.RecipeService;
import jrbj.training.cookbook.utils.ImportFromFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookbookApplication implements CommandLineRunner {

    @Value("InitialRecipes.json")
    private String importFile;

    @Autowired
    private RecipeService recipeService;

    public static void main(String[] args) {
        SpringApplication.run(CookbookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createRecipes(importFile);
    }

    private void createRecipes(String fileToImport) throws Exception {
        ImportFromFileUtils.readRecipesFromFile(fileToImport)
                .forEach(recipeService::createRecipe);
    }

}
