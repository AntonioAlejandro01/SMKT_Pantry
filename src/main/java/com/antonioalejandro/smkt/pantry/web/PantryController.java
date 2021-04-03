package com.antonioalejandro.smkt.pantry.web;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.ErrorService;
import com.antonioalejandro.smkt.pantry.model.Filter;
import com.antonioalejandro.smkt.pantry.model.FilterEnum;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.service.CategoryService;
import com.antonioalejandro.smkt.pantry.service.ProductService;
import com.antonioalejandro.smkt.pantry.utils.Constants;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class PantryController.
 */
@Slf4j
@RestController
@RequestMapping("/products")
@Api(value = "/products", tags = { "Products" })
public class PantryController {

	/** The category service. */
	@Autowired
	private CategoryService categoryService;

	/** The product service. */
	@Autowired
	private ProductService productService;

	/** The mapper list to response. */
	private final Function<List<Product>, ResponseEntity<List<Product>>> mapperListToResponse;

	/**
	 * Instantiates a new pantry controller.
	 */
	public PantryController() {
		this.mapperListToResponse = list -> {
			ResponseEntity<List<Product>> response;
			if (list.isEmpty()) {
				response = ResponseEntity.notFound().build();
			}
			response = ResponseEntity.ok(list);
			return response;
		};
	}

	/**
	 * Gets the all.
	 *
	 * @param userId the user id
	 * @return the all
	 * @throws ErrorService the error service
	 */
	@GetMapping("all")
	public ResponseEntity<List<Product>> getAll(@RequestHeader(name = "userID", required = true) final String userId)
			throws ErrorService {

		log.info("All product for {}", userId);

		return productService.allProducts(userId).map(this.mapperListToResponse)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Gets the by id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the by id
	 * @throws ErrorService the error service
	 */
	@GetMapping("id/{id}")
	public ResponseEntity<Product> getById(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id") final Long id) throws ErrorService {
		log.info("getById {}, user: {}", id, userId);
		return productService.productById(userId, id).map(product -> ResponseEntity.ok().body(product))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Search.
	 *
	 * @param userId the user id
	 * @param filter the filter
	 * @param value  the value
	 * @return the response entity
	 * @throws ErrorService the error service
	 */
	@GetMapping("search/{filter}/{value}")
	public ResponseEntity<List<Product>> search(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "filter") String filter, @PathVariable(name = "value") String value)
			throws ErrorService {
		log.info("search by {} with value {}. the user: {}", filter, value, userId);
		return productService.searchByFilter(userId, filter, value).map(this.mapperListToResponse)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	@GetMapping("categories")
	public ResponseEntity<List<Category>> getCategories() {
		log.info("Call /products/categories");
		return ResponseEntity.ok(categoryService.getCategories());
	}

	@GetMapping("filters")
	public ResponseEntity<List<Filter>> getFilter() {
		log.info("call /products/filters");
		return ResponseEntity.ok(FilterEnum.allFilters().stream()
				.sorted((c, c2) -> Integer.compare(c.getId(), c2.getId())).collect(Collectors.toList()));
	}

	/**
	 * Gets the excel.
	 *
	 * @param userId the user id
	 * @return the excel
	 * @throws ErrorService the error service
	 */
	@GetMapping(value = "/excel", produces = Constants.PRODUCES_XSL)
	public ResponseEntity<byte[]> getExcel(@RequestHeader(name = "userID", required = true) final String userId,
			@RequestHeader(name = "Authorization", required = true) final String token) throws ErrorService {
		log.info("excel for user: {}", userId);
		return productService.getExcel(userId, token).map(excel -> {
			ResponseEntity<byte[]> response;
			if (excel.length == 0) {
				response = ResponseEntity.noContent().build();
			}
			response = ResponseEntity.ok().body(excel);
			return response;
		}).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Post product.
	 *
	 * @param userId  the user id
	 * @param product the product
	 * @return the response entity
	 * @throws ErrorService the error service
	 */
	@PostMapping()
	public ResponseEntity<Product> postProduct(@RequestHeader(name = "userID", required = true) final String userId,
			@RequestBody(required = true) ProductDTO product) throws ErrorService {
		log.info("addProduct to user: {}", userId);
		return productService.addProduct(userId, product).map(prdct -> ResponseEntity.ok().body(prdct))
				.orElse(ResponseEntity.badRequest().build());
	}

	/**
	 * Put product.
	 *
	 * @param userId  the user id
	 * @param id      the id
	 * @param product the product
	 * @return the response entity
	 * @throws ErrorService the error service
	 */
	@PutMapping("{id}")
	public ResponseEntity<Product> putProduct(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id") long id, @RequestBody(required = true) ProductDTO product) throws ErrorService {
		log.info("update product with id {} by user {}", userId);
		return productService.putProduct(userId, id, product).map(p -> ResponseEntity.ok().body(p))
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Adds the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @return the response entity
	 * @throws ErrorService the error service
	 */
	@PatchMapping("add/{id}")
	public ResponseEntity<Object> addAmountToProduct(
			@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) long id,
			@RequestParam(name = "amount", required = true) int amount) throws ErrorService {
		log.info("add amount {} to product {} by user: {}", amount, id, userId);
		productService.addAmountToProduct(userId, id, amount);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Removes the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @return the response entity
	 * @throws ErrorService the error service
	 */
	@PatchMapping("remove/{id}")
	public ResponseEntity<Object> removeAmountToProduct(
			@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) long id,
			@RequestParam(name = "amount", required = true) int amount) throws ErrorService {
		log.info("remove amount {} to product {} by user: {}", amount, id, userId);
		productService.removeAmountToProduct(userId, id, amount);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Delete product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the response entity
	 * @throws ErrorService the error service
	 */
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteProduct(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) long id) throws ErrorService {
		log.info("delete product with id {} by user: {}", userId);
		productService.deleteProduct(userId, id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	/**
	 * Handle error service.
	 *
	 * @param errorService the error service
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<String> handleErrorService(final ErrorService errorService) {
		log.error("Error: {}", errorService);
		return ErrorService.getReponse(errorService);
	}

}
