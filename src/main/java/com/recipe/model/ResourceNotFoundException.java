package com.recipe.model;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resourceName, String message) {
		super(String.format("%s not found with %s", resourceName, message)); 
		
	}
	public ResourceNotFoundException(String message) {
		super(String.format(message)); 
	}

}
