package com.antonioalejandro.smkt.pantry.service;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;

/**
 * The Interface ProductService.
 */
public interface ProductService {

	/**
	 * All products.
	 *
	 * @param userId the user id
	 * @return the list
	 * @throws PantryException the error service
	 */
	public Optional<List<Product>> all(String userId);

	/**
	 * Product by id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the optional
	 * @throws PantryException the error service
	 */
	public Optional<Product> byId(String userId, String id);

	/**
	 * Search by filter.
	 *
	 * @param userId the user id
	 * @param filter the filter
	 * @param value  the value
	 * @return the optional
	 * @throws PantryException the error service
	 */
	public Optional<List<Product>> byFilter(String userId, String filter, String value) throws PantryException;

	/**
	 * Gets the excel.
	 *
	 * @param userId the user id
	 * @param token  the token
	 * @return the excel
	 * @throws PantryException the error service
	 */
	public Optional<byte[]> getExcel(String userId, String token) throws PantryException;

	/**
	 * Adds the product.
	 *
	 * @param userId  the user id
	 * @param product the product
	 * @return the optional
	 * @throws PantryException the error service
	 */
	public Optional<Product> add(String userId, ProductDTO product) throws PantryException;

	/**
	 * Put product.
	 *
	 * @param userId  the user id
	 * @param id      the id
	 * @param product the product
	 * @return the optional
	 * @throws PantryException the error service
	 */
	public Optional<Product> update(String userId, String id, ProductDTO product) throws PantryException;

	/**
	 * Adds the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @return the optional
	 * @throws PantryException the error service
	 */
	public void addAmount(String userId, String id, int amount) throws PantryException;

	/**
	 * Removes the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @return the http status
	 * @throws PantryException the error service
	 */
	public void removeAmount(String userId, String id, int amount) throws PantryException;

	/**
	 * Delete product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the http status
	 * @throws PantryException the error service
	 */
	public void delete(String userId, String id) throws PantryException;
}
