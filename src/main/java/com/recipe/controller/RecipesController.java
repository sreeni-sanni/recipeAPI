package com.recipe.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.model.Recipe;
import com.recipe.model.RecipesSearchRequest;
import com.recipe.service.RecipesService;

@RestController
public class RecipesController {

	private final RecipesService recipesService;

	public RecipesController(RecipesService recipesService) {
		this.recipesService = recipesService;
	}


	@PostMapping(path ="/addRecipe",  consumes = "application/json", produces = "application/json")
	public ResponseEntity<Recipe> addRecipe(@RequestBody @Valid Recipe recipe) throws Exception{
		return ResponseEntity.ok(recipesService.addRecipe(recipe));
		
	}
	
	@GetMapping(path = "/getAllRecipes", produces = "application/json")
	public ResponseEntity<List<Recipe>> getAllRecipes() throws Exception{
		return ResponseEntity.ok(recipesService.getAllRecipes());

	}

	@GetMapping(path = "/getRecipesByCategory", produces = "application/json")
	public ResponseEntity<List<Recipe>> getRecipesByCategory(@RequestParam("categoryType") String categoryType) throws Exception{
		return ResponseEntity.ok(recipesService.getRecipesByCategory(categoryType));

	}

	@PutMapping(path = "/updateRecipeById", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> updateRecipeById(@RequestBody Recipe updateRecipe) throws Exception{
		recipesService.updateRecipeById(updateRecipe);
		return new ResponseEntity<>("Recipe updated successfully.", HttpStatus.OK);

	}

	@DeleteMapping(path = "/removeRecipeById/{recipeId}", produces = "application/json")
	public ResponseEntity<String> removeRecipeById(@PathVariable("recipeId") String recipeId) throws Exception {
		recipesService.deleteRecipeById(recipeId);
		return new ResponseEntity<>("Recipe deleted successfully.", HttpStatus.OK);
	}
	
	@PostMapping(path ="/searchRecipesByIngredients",consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<Recipe>> searchRecipesByIngredients(@RequestBody RecipesSearchRequest recipesSearchRequest) {
		return ResponseEntity.ok(recipesService.searchRecipesByIngredients(recipesSearchRequest));

	}
}
