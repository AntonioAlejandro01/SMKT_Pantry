package com.antonioalejandro.smkt.pantry.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.Filter;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.service.CategoryService;
import com.antonioalejandro.smkt.pantry.service.FilterService;
import com.antonioalejandro.smkt.pantry.utils.Constants;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/products")
@Api(value = "/products", tags = { "Products" })
public class PantryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FilterService filterService;

	@GetMapping("all")
	public ResponseEntity<List<Product>> getAll() {
		return null;
	}

	@GetMapping("id/{id}")
	public ResponseEntity<Product> getById() {
		return null;
	}

	@GetMapping("search/{filter}/{value}")
	public ResponseEntity<List<Product>> search() {
		return null;
	}

	@GetMapping("categories")
	public ResponseEntity<List<Category>> getCategories() {
		log.info("Call /products/categories");
		return ResponseEntity.ok(categoryService.getCategories());
	}

	@GetMapping("filters")
	public ResponseEntity<List<Filter>> getFilter() {
		return ResponseEntity.ok(filterService.getFilters());
	}

	@GetMapping(value = "/excel", produces = Constants.PRODUCES_XSL)
	public ResponseEntity<byte[]> getExcel() {
		return null;
	}

	@PostMapping()
	public ResponseEntity<Product> postProduct() {
		return null;
	}

	@PutMapping("{id}")
	public ResponseEntity<Product> putProduct() {
		return null;
	}

	@RequestMapping(method = RequestMethod.HEAD, path = "{id}/add/{amount}")
	public ResponseEntity<Product> addAmountToProduct() {
		return null;
	}

	@RequestMapping(method = RequestMethod.HEAD, path = "{id}/remove/{amount}")
	public ResponseEntity<Void> removeAmountToProduct() {
		return null;
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteProduct() {
		return null;
	}

}
