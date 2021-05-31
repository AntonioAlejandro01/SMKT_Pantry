package com.antonioalejandro.smkt.pantry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.antonioalejandro.smkt.pantry.model.Product;

/**
 * Pantry Repository Class
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 */
public interface PantryRepository extends CrudRepository<Product, String> {

	@Query("{ userId: ?0 }")
	public List<Product> all(String userId);

	@Query("{ userId: ?0 , _id: ?1}")
	public Optional<Product> byId(String userId, String id);

	@Query("{ userId: ?0, name: { $regex : ?1 , $options: \"i\" } }")
	public List<Product> byName(String userId, String name);

	@Query("{ userId: ?0 , codeKey: ?1}")
	public Optional<Product> byCodeKey(String userId, String codeKey);

	@Query("{ userId: ?0, price: { $lte: ?1 } }")
	public List<Product> byPrice(String userId, double price);

	@Query("{ userId: ?0, amount: { $lte: ?1 } }")
	public List<Product> byAmount(String userId, int amount);
	
	@Query("{ userId: ?0, \"category._id\": ?1 }")
	public List<Product> byCategory(String userId, int category);
}
