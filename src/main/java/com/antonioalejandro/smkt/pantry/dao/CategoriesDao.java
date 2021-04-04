package com.antonioalejandro.smkt.pantry.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.antonioalejandro.smkt.pantry.model.Category;

/**
 * The Interface CategoriesDao.
 */
public interface CategoriesDao extends PagingAndSortingRepository<Category, Integer> {

}
