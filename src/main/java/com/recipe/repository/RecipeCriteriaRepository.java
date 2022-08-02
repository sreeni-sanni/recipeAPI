package com.recipe.repository;


import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.recipe.model.Ingredient;
import com.recipe.model.Recipe;
import com.recipe.model.RecipesSearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class RecipeCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder builder;

    public RecipeCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.builder = entityManager.getCriteriaBuilder();
    }

    public List<Recipe> recipesWithFilters(RecipesSearchRequest searchRequest) {
        CriteriaQuery<Recipe> criteriaQuery = builder.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);
        ListJoin<Recipe, Ingredient> ingredients = recipeRoot.joinList("ingredients", JoinType.INNER);
        Predicate predicate = getPredicate(searchRequest, recipeRoot, ingredients);
        criteriaQuery.where(predicate);
        criteriaQuery.distinct(true);
        TypedQuery<Recipe> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

	private Predicate getPredicate(RecipesSearchRequest searchRequest, Root<Recipe> recipeRoot,
			ListJoin<Recipe, Ingredient> ingredients) {
		List<Predicate> predicates = new ArrayList<>();

		if (Objects.nonNull(searchRequest.getCategory())) {
			predicates.add(builder.like(builder.lower(recipeRoot.get("category")).as(String.class), builder.lower(builder.literal(searchRequest.getCategory()))));
		}
		if (Objects.nonNull(searchRequest.getServings())) {
			predicates.add(builder.like(recipeRoot.get("servings"), "%" + searchRequest.getServings() + "%"));
		}
		if (Objects.nonNull(searchRequest.getInstructions())) {
			predicates.add(builder.like(builder.lower(recipeRoot.get("instructions")).as(String.class), builder.lower(builder.literal(searchRequest.getInstructions()))));
		}

		if (!CollectionUtils.isEmpty(searchRequest.getIngredients())) {
			CriteriaBuilder.In<String> inClause= builder.in(builder.lower(ingredients.get("name")).as(String.class));
			CriteriaBuilder.In<String> notInClause= builder.in(builder.lower(ingredients.get("name")).as(String.class));
			
			searchRequest.getIngredients().forEach((ingredient, aBoolean) -> {
				if (aBoolean) {
					inClause.value(builder.lower(builder.literal(ingredient)));
					predicates.add(inClause);
				} else {
					notInClause.value(builder.lower(builder.literal(ingredient)));
					predicates.add(builder.not(notInClause));
				}
			});
		}
		return builder.and(predicates.toArray(new Predicate[0]));
	}

}
