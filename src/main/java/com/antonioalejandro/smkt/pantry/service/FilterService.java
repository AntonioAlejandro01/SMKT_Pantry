package com.antonioalejandro.smkt.pantry.service;

import java.util.List;

import com.antonioalejandro.smkt.pantry.model.Filter;

/**
 * The Interface FilterService.
 */
public interface FilterService {
	
	/**
	 * Gets the filters.
	 *
	 * @return the filters
	 */
	public List<Filter> getFilters();

}
