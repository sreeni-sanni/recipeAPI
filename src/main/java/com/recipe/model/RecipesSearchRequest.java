package com.recipe.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RecipesSearchRequest {
	private String category;
	private Integer servings;
	private String instructions;
	private Map<String, Boolean> ingredients;
}
