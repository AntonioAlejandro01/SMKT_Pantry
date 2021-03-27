package com.antonioalejandro.smkt.pantry.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.antonioalejandro.smkt.pantry.model.Filter;

public interface FiltersDao extends PagingAndSortingRepository<Filter, Integer> {

}
