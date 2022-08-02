package com.recipe.controller;

import com.recipe.model.ErrorDetails;
import com.recipe.model.Ingredient;
import com.recipe.model.Recipe;
import com.recipe.model.RecipesSearchRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipesControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @Test
    public void getAllRecipesTest() {
        ResponseEntity<List> recipes = restTemplate.getForEntity("http://localhost:"+port+"/getAllRecipes", List.class);
        assertTrue(!recipes.getBody().isEmpty());
        assertEquals(recipes.getStatusCodeValue(),200);
    }

    @Test
    public void addRecipeTest() {
        Recipe recipe = new Recipe();
        recipe.setCategory("Sweet");
        recipe.setInstructions("Gas");
        recipe.setServings(String.valueOf(5));
        recipe.setRecipeName("Laddu");
        Ingredient oil = new Ingredient();
        oil.setName("Oil");
        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        Ingredient basinPowder = new Ingredient();
        basinPowder.setName("Basin_powder");
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(oil);ingredientList.add(sugar);ingredientList.add(basinPowder);
        recipe.setIngredients(ingredientList);

        ResponseEntity<Recipe> recipeResponse = restTemplate.postForEntity("http://localhost:"+port+"/addRecipe", recipe, Recipe.class);

        assertNotNull(recipeResponse.getBody().getRecipeId());
        assertEquals(recipeResponse.getBody().getIngredients().size(), 3);

    }

    @Test
    public void getRecipeByCategoryTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Recipe>> recipeByCategory = restTemplate.exchange("http://localhost:" + port + "/getRecipesByCategory?categoryType=Sweet", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Recipe>>(){});

        assertEquals(Objects.requireNonNull(recipeByCategory.getBody()).size(), 1);
        assertEquals(recipeByCategory.getBody().get(0).getRecipeName(), "Laddu");

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Ice");
        recipeByCategory.getBody().get(0).getIngredients().add(ingredient);

        assertEquals(restTemplate.exchange("http://localhost:"+port+"/updateRecipeById/",HttpMethod.PUT,  ResponseEntity.ok(recipeByCategory.getBody().get(0)), String.class).getBody(), "Recipe updated successfully.");

        String id = recipeByCategory.getBody().stream().filter(recipe -> recipe.getRecipeName().equalsIgnoreCase("Laddu")).findFirst().get().getRecipeId();

        assertEquals(restTemplate.exchange("http://localhost:"+port+"/removeRecipeById/"+id, HttpMethod.DELETE, null, String.class).getBody(), "Recipe deleted successfully.");

    }

    @Test
    public void getRecipeByCategoryForExceptionTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ErrorDetails> exception = restTemplate.exchange("http://localhost:" + port + "/getRecipesByCategory?categoryType=Fruit", HttpMethod.GET, entity, ErrorDetails.class);
        assertEquals(exception.getBody().getMessage(), "Recipes not found with category:Fruit");
    }

    @Test
    public void getRecipesBySearchTest() {
        RecipesSearchRequest recipesSearchRequest = new RecipesSearchRequest();
        recipesSearchRequest.setCategory("Fish");
        recipesSearchRequest.setServings(1);
        recipesSearchRequest.setInstructions("Oven");
        Map<String, Boolean> map = new HashMap<>();
        map.put("pepper", true);
        recipesSearchRequest.setIngredients(map);
        ResponseEntity<List> recipes = restTemplate.postForEntity("http://localhost:" + port + "/searchRecipesByIngredients", recipesSearchRequest, List.class);
        assertEquals(recipes.getStatusCodeValue(), 200);
    }

    @Test
    public void getRecipesBySearchExceptionTest() {
        RecipesSearchRequest recipesSearchRequest = new RecipesSearchRequest();
        recipesSearchRequest.setCategory("Fish");
        ResponseEntity<ErrorDetails> exception = restTemplate.postForEntity("http://localhost:" + port + "/searchRecipesByIngredients", null, ErrorDetails.class);
        assertEquals(exception.getStatusCodeValue(), 500);
    }
}
