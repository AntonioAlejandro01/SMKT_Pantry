package com.antonioalejandro.smkt.pantry.model;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.repository.PantryRepository;

/**
 * The IFunctional Interface OperationSearchProduct.
 * 
 * @author AntonioAlejandro01 -www.antonioalejandro.com
 * @version 1.0.0
 */
@FunctionalInterface
public interface OperationSearchProduct {

	/**
	 * Search a Product
	 * 
	 * @param userId
	 * @param value
	 * @param db
	 * @return
	 * @throws PantryException
	 */
	public Optional<List<Product>> search(String userId, String value, PantryRepository repo) throws PantryException;
}
