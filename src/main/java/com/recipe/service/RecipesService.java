package com.recipe.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.recipe.model.Ingredient;
import com.recipe.model.Recipe;
import com.recipe.model.RecipesSearchRequest;
import com.recipe.model.ResourceNotFoundException;
import com.recipe.repository.RecipeCriteriaRepository;
import com.recipe.repository.RecipeMangerRepository;

@Service
public class RecipesService {

	private final RecipeMangerRepository recipeMangerRepository;
	private final RecipeCriteriaRepository recipeCriteriaRepository;

	public RecipesService(RecipeMangerRepository recipeMangerRepository, RecipeCriteriaRepository recipeCriteriaRepository) {
		this.recipeMangerRepository = recipeMangerRepository;
		this.recipeCriteriaRepository = recipeCriteriaRepository;
	}

	/*
	 * 	It  will check first whether given recipe name exist or not 
	 * if it's false then it ill save recipe object into db
	 * else will throw exception
	 */
	public Recipe addRecipe(Recipe recipe) {
		if (recipeMangerRepository.existsByRecipeName(recipe.getRecipeName())) {
			throw new ResourceNotFoundException("Recipe already exist with " + recipe.getRecipeName());
		}
		return recipeMangerRepository.save(recipe);
	}
	
	/*
	 * return list of recipes
	 */
	public List<Recipe> getAllRecipes() {
		return recipeMangerRepository.findAll();
	}

	/*
	 * it will find recipes based on category
	 * if no recipes found with given category throw exception 
	 */
	public List<Recipe> getRecipesByCategory(String categoryType) {
		Optional<List<Recipe>> recipeList = recipeMangerRepository.findByCategory(categoryType);
		if (!CollectionUtils.isEmpty(recipeList.get())) {
			return recipeList.get();
		} else {
			throw new ResourceNotFoundException("Recipes","category:" + categoryType);
		}
	}
	/*
	 * first it will find recipe by recipe Id 
	 * then will update recipe with update values
	 * will merge newly added ingredients and old ingredients to identify duplicate ingredients 
	 */
	public Recipe updateRecipeById(Recipe updateRecipe) {

		Recipe recipe = getRecipe(updateRecipe.getRecipeId());

		if (!CollectionUtils.isEmpty(updateRecipe.getIngredients())) {
			recipe.setIngredients(getDistinctIngredients(updateRecipe.getIngredients(), recipe.getIngredients()));
		}
		recipe.setCategory(Optional.ofNullable(updateRecipe.getCategory()).orElse(recipe.getCategory()));
		recipe.setInstructions(Optional.ofNullable(updateRecipe.getInstructions()).orElse(recipe.getInstructions()));
		recipe.setServings(Optional.ofNullable(updateRecipe.getServings()).orElse(recipe.getServings()));
		return recipeMangerRepository.save(recipe);
	}

	private List<Ingredient> getDistinctIngredients(List<Ingredient> ingredients, List<Ingredient> newIngredients) {
		return new ArrayList<>(Stream.of(ingredients, newIngredients).flatMap(Collection::stream)
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Ingredient::getName)))));

	}

	/*
	 * this method will delete recipe by recipe Id
	 */
	public void deleteRecipeById(String recipeId) {
		recipeMangerRepository.deleteById(recipeId);
	}
	
	/*
	 * this method will perform search on recipes based on different input search request
	 */
	public List<Recipe> searchRecipesByIngredients(RecipesSearchRequest request) {
		return recipeCriteriaRepository.recipesWithFilters(request);
	}
	
	public Recipe getRecipe(String recipeId) {
		return recipeMangerRepository.findById(recipeId)
				.orElseThrow(() -> new ResourceNotFoundException("Recipe", recipeId));
	}
	 
}
