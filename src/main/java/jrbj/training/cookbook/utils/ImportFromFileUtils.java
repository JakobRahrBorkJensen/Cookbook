package jrbj.training.cookbook.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

public class ImportFromFileUtils {


    public static List<RecipeDTO> readRecipesFromFile(String fileToImport) throws IOException {
        return new ObjectMapper().setVisibility(FIELD, ANY)
                .readValue(new FileInputStream(fileToImport), new TypeReference<>() {});
    }
}
