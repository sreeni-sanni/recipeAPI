package com.recipe.service;

import com.recipe.model.Ingredient;
import com.recipe.model.Recipe;
import com.recipe.model.RecipesSearchRequest;
import com.recipe.model.ResourceNotFoundException;
import com.recipe.repository.RecipeCriteriaRepository;
import com.recipe.repository.RecipeMangerRepository;
import com.recipe.utility.RecipeHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    RecipesService service;
    @Mock
    private RecipeMangerRepository recipeMangerRepository;
    @Mock
    private RecipeCriteriaRepository recipeCriteriaRepository;

    @BeforeEach
    public void setService() {
        MockitoAnnotations.openMocks(this);
        service = new RecipesService(recipeMangerRepository, recipeCriteriaRepository);
    }

    @Test
    @DisplayName("Get all recipes")
    public void getAllRecipesTest() {
        List<Recipe> recipeList = Arrays.asList(RecipeHelper.getCreatedRecipe());
        when(recipeMangerRepository.findAll()).thenReturn(recipeList);

        List<Recipe> recipesList = service.getAllRecipes();
        assertEquals(recipesList.size(), 1);
    }

    @Test
    @DisplayName("Get all recipes")
    public void addRecipeTest() {
        when(recipeMangerRepository.existsByRecipeName(anyString())).thenReturn(false);
        when(recipeMangerRepository.save(RecipeHelper.getRecipe())).thenReturn(RecipeHelper.getCreatedRecipe());

        Recipe recipesList = service.addRecipe(RecipeHelper.getRecipe());
        assertEquals(recipesList.getRecipeName(), "Laddu");
    }

    @Test
    @DisplayName("Get exception why calling all the recipes")
    public void addRecipeExistingTest() {
        when(recipeMangerRepository.existsByRecipeName(anyString())).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () -> service.addRecipe(RecipeHelper.getRecipe()), "Recipe already exist with Laddu");
    }

    @Test
    @DisplayName("Get all recipes related to category")
    public void getRecipesByCategoryTest() {
        List<Recipe> recipeList = Arrays.asList(RecipeHelper.getCreatedRecipe());
        when(recipeMangerRepository.findByCategory(anyString())).thenReturn(Optional.of(recipeList));

        List<Recipe> recipesList = service.getRecipesByCategory("Sweet");
        assertEquals(recipesList.size(), 1);
    }

    @Test
    @DisplayName("update recipes test")
    public void updateRecipeByNameTest() {
        Recipe recipe = RecipeHelper.getCreatedRecipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Ice");
        recipe.getIngredients().add(ingredient);
        when(recipeMangerRepository.findById(recipe.getRecipeId())).thenReturn(Optional.of(recipe));
        when(recipeMangerRepository.save(recipe)).thenReturn(recipe);

        Recipe recipesList = service.updateRecipeById(recipe);
        assertEquals(recipesList.getIngredients().size(), 4);
    }

    @Test
    @DisplayName("delete recipes by id test")
    public void deleteRecipeByNameTest() {
        Recipe recipe = RecipeHelper.getCreatedRecipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Ice");
        recipe.getIngredients().add(ingredient);
        doNothing().when(recipeMangerRepository).deleteById(recipe.getRecipeId());

        service.deleteRecipeById(recipe.getRecipeId());
        verify(recipeMangerRepository, times(1)).deleteById(recipe.getRecipeId());
    }

    @Test
    @DisplayName("Search recipes test")
    public void searchFilterTest() {
        RecipesSearchRequest recipesSearchRequest = new RecipesSearchRequest();
        recipesSearchRequest.setCategory("Fish");
        recipesSearchRequest.setServings(1);
        recipesSearchRequest.setInstructions("Oven");
        Map<String, Boolean> map = new HashMap<>();
        map.put("pepper", true);
        recipesSearchRequest.setIngredients(map);

        List<Recipe> recipeList = Arrays.asList(RecipeHelper.getCreatedRecipe());

        when(recipeCriteriaRepository.recipesWithFilters(recipesSearchRequest)).thenReturn(recipeList);

        List<Recipe> listRecipes = service.searchRecipesByIngredients(recipesSearchRequest);
        assertEquals(listRecipes.size(), 1);
    }
}
