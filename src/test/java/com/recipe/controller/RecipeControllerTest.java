package com.recipe.controller;

import com.recipe.model.Recipe;
import com.recipe.model.RecipesSearchRequest;
import com.recipe.service.RecipesService;
import com.recipe.utility.RecipeHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerTest {

    RecipesController recipesController;
    @Mock RecipesService recipesService;

    @BeforeEach
    void setRecipesController() {
        recipesController = new RecipesController(recipesService);
    }

    @Test
    @DisplayName("Unit test for add recipe")
    public void testAddRecipe() throws Exception {
        when(recipesService.addRecipe(RecipeHelper.getRecipe())).thenReturn(RecipeHelper.getCreatedRecipe());

        ResponseEntity<Recipe> recipeResponseEntity = recipesController.addRecipe(RecipeHelper.getRecipe());
        assertEquals(recipeResponseEntity.getBody().getRecipeName(), "Laddu");
        assertEquals(recipeResponseEntity.getBody().getCategory(), "Sweet");
    }

    @Test
    @DisplayName("Unit test for get all recipes")
    public void testGetAllRecipes() throws Exception {
        List<Recipe> recipeList = Arrays.asList(RecipeHelper.getCreatedRecipe());
        when(recipesService.getAllRecipes()).thenReturn(recipeList);

        ResponseEntity<List<Recipe>> recipeResponseEntity = recipesController.getAllRecipes();
        assertEquals(recipeResponseEntity.getBody().size(), 1);
        assertEquals(recipeResponseEntity.getBody().get(0).getCategory(), "Sweet");
    }

    @Test
    @DisplayName("Unit test for get recipes by category")
    public void testGetRecipesByCategory() throws Exception {
        List<Recipe> recipeList = Arrays.asList(RecipeHelper.getCreatedRecipe());
        when(recipesService.getRecipesByCategory(anyString())).thenReturn(recipeList);

        ResponseEntity<List<Recipe>> recipeResponseEntity = recipesController.getRecipesByCategory("Sweet");
        assertEquals(recipeResponseEntity.getBody().size(), 1);
        assertEquals(recipeResponseEntity.getBody().get(0).getRecipeName(), "Laddu");
    }

    @Test
    @DisplayName("Unit test for get recipes by category")
    public void testSerachRecipes() throws Exception {
        RecipesSearchRequest recipesSearchRequest = new RecipesSearchRequest();
        recipesSearchRequest.setCategory("Sweet");
        recipesSearchRequest.setInstructions("Gas");
        Map<String, Boolean> booleanMap = new HashMap<>();
        booleanMap.put("Sugar", true);
        recipesSearchRequest.setIngredients(booleanMap);

        List<Recipe> recipeList = Arrays.asList(RecipeHelper.getCreatedRecipe());
        when(recipesService.searchRecipesByIngredients(recipesSearchRequest)).thenReturn(recipeList);

        ResponseEntity<List<Recipe>> recipeResponseEntity = recipesController.searchRecipesByIngredients(recipesSearchRequest);
        assertEquals(recipeResponseEntity.getBody().size(), 1);
        assertEquals(recipeResponseEntity.getBody().get(0).getRecipeName(), "Laddu");
    }
}
