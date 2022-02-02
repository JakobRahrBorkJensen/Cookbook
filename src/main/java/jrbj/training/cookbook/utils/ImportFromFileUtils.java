package jrbj.training.cookbook.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

/**
 * Utility class for all things related to import from files
 */
public class ImportFromFileUtils {

    /**
     * Loads recipes from JSON formatted text file into Recipe DTOs
     * @param fileToImport Path to file to be imported
     * @throws IOException if file is not found or readable.
     */
    public static List<RecipeDTO> readRecipesFromFile(String fileToImport) throws IOException {
        return new ObjectMapper().setVisibility(FIELD, ANY)
                .readValue(new FileInputStream(fileToImport), new TypeReference<>() {});
    }
}
