package com.antonioalejandro.smkt.pantry.service;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.pantry.model.ErrorService;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;

/**
 * The Interface ProductService.
 */
public interface ProductService {

	/**
	 * All products.
	 *
	 * @param userId the user id
	 * @return the list
	 * @throws ErrorService the error service
	 */
	public Optional<List<Product>> allProducts(String userId) throws ErrorService;

	/**
	 * Product by id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	public Optional<Product> productById(String userId, long id) throws ErrorService;

	/**
	 * Search by filter.
	 *
	 * @param userId the user id
	 * @param filter the filter
	 * @param value  the value
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	public Optional<List<Product>> searchByFilter(String userId, String filter, String value) throws ErrorService;

	/**
	 * Gets the excel.
	 *
	 * @param userId the user id
	 * @param token the token
	 * @return the excel
	 * @throws ErrorService the error service
	 */
	public Optional<byte[]> getExcel(String userId, String token) throws ErrorService;

	/**
	 * Adds the product.
	 *
	 * @param userId  the user id
	 * @param product the product
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	public Optional<Product> addProduct(String userId, ProductDTO product) throws ErrorService;

	/**
	 * Put product.
	 *
	 * @param userId  the user id
	 * @param id      the id
	 * @param product the product
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	public Optional<Product> putProduct(String userId, long id, ProductDTO product) throws ErrorService;

	/**
	 * Adds the amount to product.
	 *
	 * @return the optional
	 */
	public void addAmountToProduct(String userId, long id, int amount) throws ErrorService;

	/**
	 * Removes the amount to product.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @param amount the amount
	 * @return the http status
	 * @throws ErrorService the error service
	 */
	public void removeAmountToProduct(String userId, long id, int amount) throws ErrorService;
	
	
	/**
	 * Delete product.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @return the http status
	 */
	public void deleteProduct(String userId, long id) throws ErrorService;
}
