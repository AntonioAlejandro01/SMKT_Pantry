package com.antonioalejandro.smkt.pantry.db;

import java.util.List;
import java.util.Optional;

import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryDatabaseException;

public interface PantryDatabase {

    /**
     * Save a Product into database
     * 
     * @param product
     * @return
     */
    public Optional<Product> insertProduct(Product product);

    /**
     * Find all products for a userId
     * 
     * @param userId {@link String}
     * @return {@link Optional}&lt;{@link List}&lt;{@link Product}&gt;&gt;
     * 
     */
    public Optional<List<Product>> findAll(String userId);

    /**
     * Find a Product by id
     * 
     * @param id     {@link String}
     * @param userId {@link String}
     * @return {@link Optional}&lt;{@link Product}&gt;
     */
    public Optional<Product> findById(String id, String userId);

    /**
     * Find Products that her name contains a name
     * 
     * @param name   {@link String}
     * @param userId {@link String}
     * @return {@link Optional}&lt;{@link List}&lt;{@link Product}&gt;&gt;
     */
    public Optional<List<Product>> findByName(String name, String userId);

    /**
     * Find Product by CodeKey
     * 
     * @param userId  {@link String}
     * @param codeKey {@link String}
     * @return {@link Optional}&lt;{@link Product}&gt;
     */
    public Optional<Product> findByCodeKey(String userId, String codeKey);

    /**
     * Find a Product that her price is equals or less than passed as parameter
     * 
     * @param userId {@code String}
     * @param price  {@code double}
     * @return {@link Optional}&lt;{@link List}&lt;{@link Product}&gt;&gt;
     */
    public Optional<List<Product>> findByPrice(String userId, double price);

    /**
     * Find Products by category id
     * 
     * @param userId     {@code String}
     * @param categoryId {@code int}
     * @return {@link Optional}&lt;{@link List}&lt;{@link Product}&gt;&gt;
     */
    public Optional<List<Product>> findByCategory(String userId, int categoryId);

    /**
     * Find Products that her amount is equals or less than passed as parameter
     * 
     * @param userId
     * @param amount
     * @return
     */
    public Optional<List<Product>> findByAmount(String userId, int amount);

    /**
     * Add amount to one product
     * 
     * @param userId
     * @param id
     * @param amount
     * @return
     */
    public boolean addAmountById(String userId, String id, int amount);

    /**
     * Remove amount to one product. If the amount will be negative the amount
     * doesn't update
     * 
     * @param userId
     * @param id
     * @param amount
     * @throws PantryDatabaseException
     * @return
     */
    public boolean removeAmountById(String userId, String id, int amount) throws PantryDatabaseException;

    /**
     * Delete a Product by id
     * 
     * @param userId
     * @param id
     * @return
     */
    public boolean deleteProduct(String userId, String id);

}
