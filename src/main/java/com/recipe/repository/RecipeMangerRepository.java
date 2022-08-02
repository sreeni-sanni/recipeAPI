package com.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.recipe.model.Recipe;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface RecipeMangerRepository extends JpaRepository<Recipe, String> {
	
	@Query(value = "select r from Recipe r where lower(r.category)=lower(?1)")
	Optional<List<Recipe>> findByCategory(String category);

	Boolean existsByRecipeName(String recipeName);

}
