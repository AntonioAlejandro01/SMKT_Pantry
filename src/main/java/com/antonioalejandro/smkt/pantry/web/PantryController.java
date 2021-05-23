package com.antonioalejandro.smkt.pantry.web;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.antonioalejandro.smkt.pantry.model.Filter;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.enums.CategoryEnum;
import com.antonioalejandro.smkt.pantry.model.enums.FilterEnum;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.service.ProductService;
import com.antonioalejandro.smkt.pantry.utils.Constants;
import com.antonioalejandro.smkt.pantry.utils.Validations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;


/**
 * The Class PantryController.
 */

/** The Constant log. */

/** The Constant log. */
@Slf4j
@RestController
@RequestMapping("/products")
@Api(value = "/products", tags = { "Products" })
public class PantryController {

	/** The product service. */
	@Autowired
	private ProductService productService;

	/** The validations. */
	@Autowired
	private Validations validations;
	
	/** The mapper list to response. */
	private final Function<List<Product>, ResponseEntity<List<Product>>> mapperListToResponse;

	/**
	 * Instantiates a new pantry controller.
	 */
	public PantryController() {
		this.mapperListToResponse = list -> {
			if (list.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(list);
		};
	}

	/**
	 * Gets the all.
	 *
	 * @param userId the user id
	 * @return the all
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Get all Product for user in token", responseContainer = "List", httpMethod = "GET", notes = "Get all Products that the user in the token is owner")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Product.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized") })
	@GetMapping("all")
	public ResponseEntity<List<Product>> getAll(@RequestHeader(name = "userID", required = false) final String userId)
			throws PantryException {

		log.info("getAll----- user: {}", userId);
		return productService.all(userId).map(this.mapperListToResponse).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * Gets the by id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the by id
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Get Product by id", httpMethod = "GET", notes = "Get a product by id")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id for search", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = { @ApiResponse(code = 200, response = Product.class, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("id/{id}")
	public ResponseEntity<Product> getById(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) final String id) throws PantryException {
		log.info("getById--- id: {}, user: {}", id, userId);
		validations.id(id);
		return productService.byId(userId, id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Search.
	 *
	 * @param userId the user id
	 * @param filter the filter
	 * @param value  the value
	 * @return the response entity
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Search a Product by a filter", httpMethod = "GET", notes = "Get all Product that matches with filter and value for this filter")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "filter", value = "The type of filter", required = true, type = "string", allowableValues = "NAME, CATEGORY, CODEKEY, PRICE, AMOUNT", readOnly = true, paramType = "Path"),
			@ApiImplicitParam(name = "value", value = "the value for filter", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Product.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("search/{filter}/{value}")
	public ResponseEntity<List<Product>> search(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "filter") String filter, @PathVariable(name = "value") String value)
			throws PantryException {
		log.info("search by {} with value {}. the user: {}", filter, value, userId);
		validations.value(value);
		return productService.byFilter(userId, filter, value).map(this.mapperListToResponse)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	@ApiOperation(value = "Get all Categories", httpMethod = "GET", notes = "Get all Categories")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"), })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Category.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized") })
	@GetMapping("categories")
	public ResponseEntity<List<Category>> getCategories() {
		log.info("Call /products/categories");
		return ResponseEntity
				.ok(Stream.of(CategoryEnum.values()).map(CategoryEnum::toCategory).collect(Collectors.toList()));
	}

	/**
	 * Gets the filter.
	 *
	 * @return the filter
	 */
	@ApiOperation(value = "Get all Filter", httpMethod = "GET", notes = "Get all Filters")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"), })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Filter.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized") })
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
	 * @param token  the token
	 * @return the excel
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Get Excel File ", httpMethod = "GET", notes = "Get excel file for all products that the user in token have it", produces = Constants.PRODUCES_XSL)
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"), })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "No Content", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@GetMapping(value = "/excel", produces = Constants.PRODUCES_XSL)
	public ResponseEntity<byte[]> getExcel(@RequestHeader(name = "userID", required = true) final String userId,
			@RequestHeader(name = "Authorization", required = true) final String token) throws PantryException {
		log.info("excel for user: {}", userId);
		return productService.getExcel(userId, token).map(excel -> {
			ResponseEntity<byte[]> response;
			if (excel.length == 0) {
				response = ResponseEntity.noContent().build();
			} else {
				response = ResponseEntity.ok().body(excel);				
			}
			return response;
		}).orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Post product.
	 *
	 * @param userId  the user id
	 * @param product the product
	 * @return the response entity
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Create a new Product", httpMethod = "POST", notes = "Create a new Product, the owner is the user in the token")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"), })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Product.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@PostMapping("/")
	public ResponseEntity<Product> postProduct(@RequestHeader(name = "userID", required = true) final String userId,
			@RequestBody(required = true) ProductDTO product) throws PantryException {
		log.info("addProduct to user: {}", userId);
		validations.product(product);
		return productService.add(userId, product).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
	}

	/**
	 * Put product.
	 *
	 * @param userId  the user id
	 * @param id      the id
	 * @param product the product
	 * @return the response entity
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Update a Product by id", httpMethod = "PUT", notes = "Update a Product by id")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Product.class, responseContainer = "List", message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found") })
	@PutMapping("{id}")
	public ResponseEntity<Product> putProduct(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id") String id, @RequestBody(required = true) ProductDTO product)
			throws PantryException {
		log.info("update product with id {} by user {}", userId);
		validations.id(id);
		validations.product(product);
		return productService.update(userId, id, product).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Adds the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @return the response entity
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Add amount for one product", httpMethod = "PATCH", notes = "Shortcut for add amount to one product")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 403, message = "Forbidden", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error", response = PantryException.JSONServiceError.class) })
	@PatchMapping("add/{id}")
	public ResponseEntity<Object> addAmountToProduct(
			@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "amount", required = true) int amount) throws PantryException {
		log.info("add amount {} to product {} by user: {}", amount, id, userId);
		validations.id(id);
		validations.amount(amount);
		productService.addAmount(userId, id, amount);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Removes the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @return the response entity
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Remove amount for one product", httpMethod = "PATCH", notes = "Shortcut for remove amount to one product")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = { @ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 400, message = "Bad Request", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = PantryException.JSONServiceError.class),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error", response = PantryException.JSONServiceError.class) })
	@PatchMapping("remove/{id}")
	public ResponseEntity<Object> removeAmountToProduct(
			@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "amount", required = true) int amount) throws PantryException {
		log.info("remove amount {} to product {} by user: {}", amount, id, userId);
		validations.id(id);
		validations.amount(amount);
		productService.removeAmount(userId, id, amount);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Delete product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the response entity
	 * @throws PantryException the error service
	 */
	@ApiOperation(value = "Delete a Product", httpMethod = "DELETE", notes = "Delete a Product by id")
	@ApiImplicitParams(value = {
			@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "userID", value = "Username for the user in token. The value autofill.", required = false, type = "string", readOnly = true, paramType = "Header"),
			@ApiImplicitParam(name = "id", value = "The id", required = true, type = "string", readOnly = true, paramType = "Path") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 403, message = "Forbidden", response = PantryException.JSONServiceError.class) })
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteProduct(@RequestHeader(name = "userID", required = true) final String userId,
			@PathVariable(name = "id", required = true) String id) throws PantryException {
		log.info("delete product with id {} by user: {}", userId);
		validations.id(id);
		productService.delete(userId, id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	/**
	 * Handle error service.
	 *
	 * @param errorService the error service
	 * @return the response entity
	 */
	@ExceptionHandler
	public ResponseEntity<String> handleErrorService(final PantryException errorService) {
		log.error("Error: {}", errorService);
		return errorService.toResponse();
	}

}
