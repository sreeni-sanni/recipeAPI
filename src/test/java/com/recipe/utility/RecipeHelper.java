package com.recipe.utility;

import com.recipe.model.Ingredient;
import com.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeHelper {

    public static Recipe getRecipe() {
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

        return recipe;
    }

    public static Recipe getCreatedRecipe() {
        Recipe recipe = new Recipe();
        recipe.setCategory("Sweet");
        recipe.setInstructions("Gas");
        recipe.setServings(String.valueOf(5));
        recipe.setRecipeName("Laddu");
        recipe.setRecipeId(UUID.randomUUID().toString());
        Ingredient oil = new Ingredient();
        oil.setIngredientId(UUID.randomUUID());
        oil.setName("Oil");
        Ingredient sugar = new Ingredient();
        sugar.setIngredientId(UUID.randomUUID());
        sugar.setName("sugar");
        Ingredient basinPowder = new Ingredient();
        basinPowder.setIngredientId(UUID.randomUUID());
        basinPowder.setName("Basin_powder");
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(oil);ingredientList.add(sugar);ingredientList.add(basinPowder);
        recipe.setIngredients(ingredientList);

        return recipe;
    }
}
