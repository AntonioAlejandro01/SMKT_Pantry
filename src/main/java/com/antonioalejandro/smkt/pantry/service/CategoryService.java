package com.antonioalejandro.smkt.pantry.service;

import java.util.List;
import java.util.Optional;

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
	
	/**
	 * Gets the category by id.
	 *
	 * @param id the id
	 * @return the category by id
	 */
	public Optional<Category> getCategoryById(int id);

}
