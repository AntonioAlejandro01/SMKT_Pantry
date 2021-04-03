package com.antonioalejandro.smkt.pantry.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.antonioalejandro.smkt.pantry.model.Product;

/**
 * The Interface ProductDao.
 */
public interface ProductDao extends PagingAndSortingRepository<Product, Integer> {

	/**
	 * Find all.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId", nativeQuery = true)
	public List<Product> findAll(@Param("userId") String userId);

	/**
	 * Find by id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the optional
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId AND p.id = :id", nativeQuery = true)
	public Product findById(@Param("userId") String userId, @Param("id") long id);

	/**
	 * Find by name.
	 *
	 * @param userId the user id
	 * @param name   the name
	 * @return the optional
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId AND p.name = :name", nativeQuery = true)
	public List<Product> searchByName(@Param("userId") String userId, @Param("name") String name);

	/**
	 * Find by cateogry.
	 *
	 * @param userId   the user id
	 * @param category the category
	 * @return the list
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId AND p.category = :category", nativeQuery = true)
	public List<Product> searchByCateogry(@Param("userId") String userId, @Param("category") int category);
	
	/**
	 * Search by codekey.
	 *
	 * @param userId the user id
	 * @param codekey the codekey
	 * @return the list
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId AND p.codekey = :codekey", nativeQuery = true)
	public List<Product> searchByCodekey(@Param("userId") String userId, @Param("codekey") String codekey);
	
	/**
	 * Search by price.
	 *
	 * @param userId the user id
	 * @param price the price
	 * @return the list
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId AND p.price <= :price", nativeQuery = true)
	public List<Product> searchByPrice(@Param("userId") String userId, @Param("price") double price);
	
	/**
	 * Find by name.
	 *
	 * @param userId the user id
	 * @param amount the amount
	 * @return the list
	 */
	@Query(value = "SELECT * FROM products AS p WHERE p.userId = :userId AND p.amount <= :amount", nativeQuery = true)
	public List<Product> searchByAmount(@Param("userId") String userId, @Param("amount") int amount);

}
