package jrbj.training.cookbook.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import jrbj.training.cookbook.exception.CookbookResponseEntityExceptionHandler;
import jrbj.training.cookbook.ingredient.data.IngredientEntity;
import jrbj.training.cookbook.ingredient.dto.IngredientDTO;
import jrbj.training.cookbook.ingredient.dto.MeasurementUnit;
import jrbj.training.cookbook.recipe.data.RecipeEntity;
import jrbj.training.cookbook.recipe.dto.RecipeDTO;
import jrbj.training.cookbook.recipe.dto.RecipeTitleDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link RecipeController}.
 *
 * <p>NOTE: This is a web slice - effectively mocking all interaction with services/repositories.
 */
@WebMvcTest(RecipeController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RecipeController.class, CookbookResponseEntityExceptionHandler.class})
class RecipeControllerIntegrationTest {

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    /** Checks that an HTTP 201 response is returned containing valid order JSON. */
    @Test
    public void createRecipe_whenServiceReturnsSuccessfully_thenReturnsJsonResponseWithCreatedRecipe() throws Exception {
        // Given
        var recipe = validSampleRecipeEntity();
        when(recipeService.createRecipe(any())).thenReturn(recipe);

        // When
        post("/recipes", recipe)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(recipe.getTitle()))
                .andExpect(jsonPath("summary").value(recipe.getSummary()))
                .andExpect(jsonPath("description").value(recipe.getDescription()))
                .andExpect(jsonPath("estimatedTime").value(recipe.getEstimatedTime()))
                .andExpect(jsonPath("people").value(recipe.getPeople()))
                .andExpect(jsonPath("link").value(recipe.getLink()))
                .andExpect(jsonPath("ingredients[0].id").isNotEmpty())
                .andExpect(jsonPath("ingredients[0].name").value(recipe.getIngredients().get(0).getName()))
                .andExpect(jsonPath("ingredients[0].amount").value(recipe.getIngredients().get(0)
                        .getAmount()))
                .andExpect(jsonPath("ingredients[0].unit").value(recipe.getIngredients().get(0)
                        .getUnit().getShorthand()));
    }

    /** Checks that an HTTP 500 response is returned containing no details of the internal error */
    @Test
    public void createRecipe_whenServiceReturnsWithException_thenReturnsNoDetailsAndStatus500() throws Exception {
        // Given
        var recipe = validSampleRecipeEntity();
        when(recipeService.createRecipe(any())).thenThrow(new RuntimeException("My error"));

        // When
        post("/recipes", recipe)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("date_time").isNotEmpty())
                .andExpect(jsonPath("status").value(500))
                .andExpect(jsonPath("title").value("Internal Server Error"))
                .andExpect(jsonPath("code").value("internal-server-error"))
                .andExpect(jsonPath("details").isEmpty());
    }

    /** Checks that an HTTP 400 response is returned containing details of the error made by client */
    @Test
    public void createRecipe_whenInvalidRecipeIsProvided_thenReturnsWithBadRequestWithDetails() throws Exception {
        // Given
        var recipe = invalidSampleEntity();

        // When
        post("/recipes", recipe)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("date_time").isNotEmpty())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("title").value("Bad Request"))
                .andExpect(jsonPath("code").value("bad-request.invalid-arguments"))
                .andExpect(jsonPath("details").value(containsInAnyOrder(
                        "[title] Title of recipe should be set",
                        "[description] Description of recipe should be set"))
                );
    }

    /** Checks that a HTTP 200 response is returned containing recipes when no request params is set. */
    @Test
    public void getRecipesContainingIngredients_whenNotProvidingRequestParams_thenReturnsRecipes()
            throws Exception {
        // Given
        List<RecipeTitleDTO> recipeTitleDtos = new ArrayList<>(Collections.singletonList(
                validSampleRecipeTitleDto()));
        when(recipeService.getRecipesContainingIngredients(any(), any(), any())).thenReturn(recipeTitleDtos);

        // When
        get("/recipes")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recipeTitleDtos.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(recipeTitleDtos.get(0).getTitle()));
    }

    /** Checks that a HTTP 200 response is returned containing recipes when only search_strategy param is set. */
    @Test
    public void getRecipesContainingIngredients_whenOnlySearchStrategyIsProvided_thenReturnsRecipes()
            throws Exception {
        // Given
        List<RecipeTitleDTO> recipeTitleDtos = new ArrayList<>(Collections.singletonList(
                validSampleRecipeTitleDto()));
        when(recipeService.getRecipesContainingIngredients(any(), any(), any())).thenReturn(recipeTitleDtos);

        // When
        get("/recipes?search_strategy=any")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recipeTitleDtos.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(recipeTitleDtos.get(0).getTitle()));
    }

    /** Checks that a HTTP 200 response is returned containing recipes when only ingredients param is set. */
    @Test
    public void getRecipesContainingIngredients_whenOnlyIngredientsIsProvided_thenReturnsRecipes()
            throws Exception {
        // Given
        List<RecipeTitleDTO> recipeTitleDtos = new ArrayList<>(Collections.singletonList(
                validSampleRecipeTitleDto()));
        when(recipeService.getRecipesContainingIngredients(any(), any(), any())).thenReturn(recipeTitleDtos);

        // When
        get("/recipes?ingredients=bacon")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recipeTitleDtos.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(recipeTitleDtos.get(0).getTitle()));
    }

    /** Checks that a HTTP 200 response is returned containing recipes when all params are set. */
    @Test
    public void getRecipesContainingIngredients_whenParamsAreProvided_thenReturnsRecipes()
            throws Exception {
        // Given
        List<RecipeTitleDTO> recipeTitleDtos = new ArrayList<>(Collections.singletonList(
                validSampleRecipeTitleDto()));
        when(recipeService.getRecipesContainingIngredients(any(), any(), any())).thenReturn(recipeTitleDtos);

        // When
        get("/recipes?ingredients=bacon&search_strategy=any")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recipeTitleDtos.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(recipeTitleDtos.get(0).getTitle()));
    }

    /** Checks that a HTTP 400 response is returned containing error details when invalid search
     * strategy is provided. */
    @Test
    public void getRecipesContainingIngredients_whenSearchStrategyIsInvalid_thenThrowWithDetails()
            throws Exception {
        // Given
        List<RecipeTitleDTO> recipeTitleDtos = new ArrayList<>(Collections.singletonList(
                validSampleRecipeTitleDto()));
        when(recipeService.getRecipesContainingIngredients(any(), any(), any())).thenReturn(recipeTitleDtos);

        // When
        get("/recipes?ingredients=bacon&search_strategy=invalid")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("details").value(containsInAnyOrder(
                        "Provided search strategy unknown. Allowed strategies: [ALL, ANY, NONE]")));
    }

    /**
     * Exercise the MockMvc endpoint using POST
     */
    private ResultActions post(String endpoint, Object body) throws Exception {
        var bodyAsString = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(body);

        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(endpoint)
                        .content(bodyAsString)
                        .contentType(APPLICATION_JSON_VALUE));
    }

    /**
     * Exercise the MockMvc endpoint using GET
     */
    private ResultActions get(String endpoint) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(endpoint).contentType(APPLICATION_JSON_VALUE));
    }

    /**
     * Creates a valid sample recipe
     */
    private RecipeEntity validSampleRecipeEntity() {
        var recipe = new RecipeEntity(
                1L,
                "title",
                "summary",
                "description",
                new ArrayList<>(),
                5,
                2,
                "link");
        var ingredient = new IngredientEntity(1L, "name", 2.5, MeasurementUnit.STK, null);
        recipe.addIngredient(ingredient);
        return recipe;
    }

    /**
     * Creates an invalid sample recipe, where title and description are null (which is invalid)
     */
    private RecipeDTO invalidSampleEntity() {
        List<IngredientDTO> ingredients = new ArrayList<>();
        ingredients.add(IngredientDTO.builder()
                .amount(2.5)
                .unit(MeasurementUnit.STK)
                .build());

        return RecipeDTO.builder()
                .summary("summary")
                .ingredients(ingredients)
                .estimatedTime(5)
                .people(2)
                .link("link")
                .build();
    }

    private RecipeTitleDTO validSampleRecipeTitleDto() {
        return new RecipeTitleDTO() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getTitle() {
                return "title1";
            }
        };
    }
}