package com.recipe.model;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Data;

@Data
@Entity
@Table(name="recipes")
public class Recipe {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String recipeId;
	
	@Column(name = "recipeName", nullable = false, unique = true)
	@NotEmpty(message = "RecipeName can't be blank")
	private String recipeName;
	
	@Column
	@NotEmpty(message = "Category can't be blank")
	private String category;

	@Column
	private String instructions;

	@Column
	private String servings;

	@OneToMany(targetEntity = Ingredient.class, cascade = { CascadeType.ALL })
	@PrimaryKeyJoinColumn(name = "recipeName", referencedColumnName = "recipeId")
	@NotEmpty(message = "Ingredients can't be empty")
	private List<Ingredient> ingredients;

}
