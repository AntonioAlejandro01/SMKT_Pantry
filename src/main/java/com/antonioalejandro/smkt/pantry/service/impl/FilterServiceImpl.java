package com.antonioalejandro.smkt.pantry.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.pantry.dao.FiltersDao;
import com.antonioalejandro.smkt.pantry.model.Filter;
import com.antonioalejandro.smkt.pantry.service.FilterService;

@Service
public class FilterServiceImpl implements FilterService {
	@Autowired
	private FiltersDao repository;

	@Override
	public List<Filter> getFilters() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.sorted((c, c2) -> Integer.compare(c.getId(), c2.getId())).collect(Collectors.toList());
	}
}
