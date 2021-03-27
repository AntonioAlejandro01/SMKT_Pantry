package com.antonioalejandro.smkt.pantry.service;

import java.util.List;

import com.antonioalejandro.smkt.pantry.model.Category;

/**
 * The Interface CategoryService.
 */
public interface CategoryService {
	
	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	public List<Category> getCategories();

}
