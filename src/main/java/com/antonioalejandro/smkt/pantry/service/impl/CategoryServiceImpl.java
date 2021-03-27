package com.antonioalejandro.smkt.pantry.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.pantry.dao.CategoriesDao;
import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class CategoryServiceImpl.
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	/** The repository. */
	@Autowired
	private CategoriesDao repository;

	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	@Override
	public List<Category> getCategories() {
		log.info("Service: All categories");
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.sorted((Category c, Category c2) -> Integer.compare(c.getId(), c2.getId()))
				.collect(Collectors.toList());
	}
}
