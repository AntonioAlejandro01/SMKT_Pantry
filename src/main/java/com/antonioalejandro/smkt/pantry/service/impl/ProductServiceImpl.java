package com.antonioalejandro.smkt.pantry.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.pantry.dao.ProductDao;
import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.ErrorService;
import com.antonioalejandro.smkt.pantry.model.FilterEnum;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.service.CategoryService;
import com.antonioalejandro.smkt.pantry.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The Class ProductServiceImpl.
 */
@Service

/** The Constant log. */
@Slf4j
public class ProductServiceImpl implements ProductService {

	/** The Constant HEADER_AUTH. */
	private static final String HEADER_AUTH = "Authorization";

	/** The Constant TEMPLATE_URL. */
	private static final String TEMPLATE_URL = "http://%s:%s/excel/products";

	/** The repository. */
	@Autowired
	private ProductDao repository;

	/** The category service. */
	@Autowired
	private CategoryService categoryService;

	/** The discovery client. */
	@Autowired
	private DiscoveryClient discoveryClient;

	/**
	 * All products.
	 *
	 * @param userId the user id
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	@Override
	public Optional<List<Product>> allProducts(String userId) throws ErrorService {
		return Optional.ofNullable(repository.findAll(userId));
	}

	/**
	 * Product by id.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	@Override
	public Optional<Product> productById(String userId, long id) throws ErrorService {
		return Optional.ofNullable(repository.findById(userId, id));
	}

	/**
	 * Search by filter.
	 *
	 * @param userId the user id
	 * @param filter the filter
	 * @param value the value
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	@Override
	public Optional<List<Product>> searchByFilter(String userId, String filter, String value) throws ErrorService {
		return Optional
				.ofNullable(FilterEnum.fromName(filter).getFunctionForSearch().search(userId, value, repository));

	}

	/**
	 * Gets the excel.
	 *
	 * @param userId the user id
	 * @param token the token
	 * @return the excel
	 * @throws ErrorService the error service
	 */
	@Override
	public Optional<byte[]> getExcel(String userId, String token) throws ErrorService {
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(getBodyForExcel(userId),
				MediaType.parse("application/json; charset=utf-8"));
		Request req = new Request.Builder().url(getUrl()).post(body).addHeader(HEADER_AUTH, token).build();

		try (Response response = client.newCall(req).execute()) {
			if (response.code() == HttpStatus.OK.value()) {
				return Optional.of(response.body().byteStream().readAllBytes());
			} else if (response.code() == HttpStatus.UNAUTHORIZED.value() || response.code() == HttpStatus.BAD_REQUEST.value()) {
				log.debug("Message {}",response.body().string());
				throw new ErrorService(HttpStatus.UNAUTHORIZED, "You can't do this operation or token is expired");
			} else {
				HttpStatus status = HttpStatus.valueOf(response.code());
				throw new ErrorService(status, status.getReasonPhrase());
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new ErrorService(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Adds the product.
	 *
	 * @param userId the user id
	 * @param product the product
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	@Override
	public Optional<Product> addProduct(String userId, ProductDTO product) throws ErrorService {

		Optional<Category> oCategory = categoryService.getCategoryById(product.getCategory());

		if (oCategory.isEmpty()) {
			throw new ErrorService(HttpStatus.BAD_REQUEST, "The category is not valid.");
		}
		Category category = oCategory.get();

		Product productToSave = new Product();
		productToSave.setAmount(product.getAmount());
		productToSave.setCategory(category);
		productToSave.setCodeKey(product.getCodeKey());
		productToSave.setName(product.getName());
		productToSave.setPrice(product.getPrice());
		productToSave.setUserId(userId);

		return Optional.ofNullable(repository.save(productToSave));
	}

	/**
	 * Put product.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @param product the product
	 * @return the optional
	 * @throws ErrorService the error service
	 */
	@Override
	public Optional<Product> putProduct(String userId, long id, ProductDTO product) throws ErrorService {
		Optional<Product> productSaved = Optional.of(repository.findById(userId, id));
		if (productSaved.isEmpty()) {
			throw new ErrorService(HttpStatus.FORBIDDEN, "The id is not valid or you haven't got grants");
		}
		// the product exists and have permissions
		Product productToUpdate = productSaved.get();
		// check category
		if (product.getCategory() != productToUpdate.getCategory().getId()) {
			// the category maybe can updated
			Optional<Category> category = categoryService.getCategoryById(product.getCategory());
			if (category.isEmpty()) {
				throw new ErrorService(HttpStatus.BAD_REQUEST, "The category is not valid.");
			}
			productToUpdate.setCategory(category.get());
		}
		productToUpdate.setAmount(product.getAmount());
		productToUpdate.setCodeKey(product.getCodeKey());
		productToUpdate.setName(product.getName());
		productToUpdate.setPrice(product.getPrice());

		return Optional.ofNullable(repository.save(productToUpdate));
	}

	/**
	 * Adds the amount to product.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @param amount the amount
	 * @throws ErrorService the error service
	 */
	@Override
	public void addAmountToProduct(String userId, long id, int amount) throws ErrorService {
		Optional<Product> oProduct = Optional.ofNullable(repository.findById(userId, id));
		if (oProduct.isEmpty()) {
			throw new ErrorService(HttpStatus.FORBIDDEN, "The id is not valid or you haven't got grants");
		}

		Product product = oProduct.get();

		product.setAmount(product.getAmount() + amount);

		if (Optional.ofNullable(repository.save(product)).isEmpty()) {
			throw new ErrorService(HttpStatus.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}

	}

	/**
	 * Removes the amount to product.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @param amount the amount
	 * @throws ErrorService the error service
	 */
	@Override
	public void removeAmountToProduct(String userId, long id, int amount) throws ErrorService {
		Optional<Product> oProduct = Optional.ofNullable(repository.findById(userId, id));
		if (oProduct.isEmpty()) {
			throw new ErrorService(HttpStatus.FORBIDDEN, "The id is not valid or you haven't got grants");
		}

		Product product = oProduct.get();

		if (product.getAmount() - amount < 0) {
			throw new ErrorService(HttpStatus.BAD_REQUEST, "The amount is not enough");
		}
		
		product.setAmount(product.getAmount() - amount);

		if (Optional.ofNullable(repository.save(product)).isEmpty()) {
			throw new ErrorService(HttpStatus.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}
	}

	/**
	 * Delete product.
	 *
	 * @param userId the user id
	 * @param id the id
	 * @throws ErrorService the error service
	 */
	@Override
	public void deleteProduct(String userId, long id) throws ErrorService {
		Optional<Product> oProduct = Optional.ofNullable(repository.findById(userId, id));
		if (oProduct.isEmpty()) {
			throw new ErrorService(HttpStatus.FORBIDDEN, "The id is not valid or you haven't got grants");
		}
		repository.delete(oProduct.get());
	}

	/**
	 * Gets the body for excel.
	 *
	 * @param userId the user id
	 * @return the body for excel
	 * @throws ErrorService the error service
	 */
	private String getBodyForExcel(String userId) throws ErrorService {
		Optional<List<Product>> products = allProducts(userId);
		if (products.isEmpty() || products.get().isEmpty()) {
			throw new ErrorService(HttpStatus.NO_CONTENT,
					String.format("The user %s haven't got any products", userId));
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(products.get());
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage(), e);
			throw new ErrorService(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	private String getUrl() {
		ServiceInstance instanceInfo = discoveryClient.getInstances("smkt-files").get(0);
		return String.format(TEMPLATE_URL, instanceInfo.getHost(), instanceInfo.getPort());
	}

}
