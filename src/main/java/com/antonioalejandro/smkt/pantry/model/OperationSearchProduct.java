package com.antonioalejandro.smkt.pantry.model;

import java.util.List;

import com.antonioalejandro.smkt.pantry.dao.ProductDao;

/**
 * The Interface OperationSearchProduct.
 */
@FunctionalInterface
public interface OperationSearchProduct {

	/**
	 * Search.
	 *
	 * @param userId the user id
	 * @param value  the value
	 * @param dao    the dao
	 * @return the list
	 * @throws ErrorService the error service
	 */
	public List<Product> search(String userId, String value, ProductDao dao) throws ErrorService;
}
