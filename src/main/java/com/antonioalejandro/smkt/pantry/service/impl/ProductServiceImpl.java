package com.antonioalejandro.smkt.pantry.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.pantry.db.PantryDatabase;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.enums.CategoryEnum;
import com.antonioalejandro.smkt.pantry.model.enums.FilterEnum;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.service.ProductService;
import com.antonioalejandro.smkt.pantry.utils.UUIDGenerator;
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
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @see ProductService
 * @see UUIDGenerator
 * @version 1.0.0
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService, UUIDGenerator {

	/** The Constant HEADER_AUTH. */
	private static final String HEADER_AUTH = "Authorization";

	/** The Constant TEMPLATE_URL. */
	private static final String TEMPLATE_URL = "http://%s:%s/excel/products";

	/** The repository. */
	@Autowired
	private PantryDatabase db;

	/** The discovery client. */
	@Autowired
	private DiscoveryClient discoveryClient;

	@Value("${id_files_instance}")
	private String idFileInstance;

	/**
	 * All products.
	 *
	 * @param userId the user id
	 * @return the optional
	 * @throws PantryException the error service
	 */
	@Override
	public Optional<List<Product>> all(String userId) {
		log.info("---> ProductService-----findAll---- userId: {}", userId);
		return db.findAll(userId);
	}

	/**
	 * Product by id.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @return the optional
	 * @throws PantryException the error service
	 */
	@Override
	public Optional<Product> byId(String userId, String id) {
		log.info("---> ProductService-----findById---- userId: {}, id: {}", userId, id);
		return db.findById(userId, id);
	}

	/**
	 * Search by filter.
	 *
	 * @param userId the user id
	 * @param filter the filter
	 * @param value  the value
	 * @return the optional
	 * @throws PantryException the error service
	 */
	@Override
	public Optional<List<Product>> byFilter(String userId, String filter, String value) throws PantryException {
		log.info("---> ProductService-----findByFilter---- userId: {}, filter: {}, value: {}", userId, filter, value);
		return FilterEnum.fromName(filter).getFunctionForSearch().search(userId, value, db);

	}

	/**
	 * Gets the excel.
	 *
	 * @param userId the user id
	 * @param token  the token
	 * @return the excel
	 * @throws PantryException the error service
	 */
	@Override
	public Optional<byte[]> getExcel(String userId, String token) throws PantryException {
		log.info("---> ProductService-----getExcel---- userId: {}, token: {}", userId, token);
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(getBodyForExcel(userId),
				MediaType.parse("application/json; charset=utf-8"));
		log.debug("getExcel-----CREATE BODY_REQUEST");
		Request req = new Request.Builder().url(getUrl()).post(body).addHeader(HEADER_AUTH, token).build();
		log.debug("getExcel-----CREATE REQUEST");
		try (Response response = client.newCall(req).execute()) {
			log.info("Call Response success");
			if (response.code() == HttpStatus.OK.value()) {
				log.info("getExcel----RESPONSE CALL STATUS OK");
				return Optional.of(response.body().byteStream().readAllBytes());
			} else if (response.code() == HttpStatus.UNAUTHORIZED.value()
					|| response.code() == HttpStatus.BAD_REQUEST.value()) {
				log.error("getExcel-----RESPONSE CALL STATUS {}", response.code());
				log.debug("getExcel-----Message {}", response.body().string());
				throw new PantryException(HttpStatus.UNAUTHORIZED, "You can't do this operation or token is expired");
			} else {
				log.error("getExcel-----RESPONSE STATUS ERROR {}", response.code());
				HttpStatus status = HttpStatus.valueOf(response.code());
				throw new PantryException(status, status.getReasonPhrase());
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new PantryException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Adds the product.
	 *
	 * @param userId  the user id
	 * @param product the product
	 * @return the optional
	 * @throws PantryException the error service
	 */
	@Override
	public Optional<Product> add(String userId, ProductDTO product) throws PantryException {
		log.info("add-----userId: {}, product: {}", userId, product);
		Optional<CategoryEnum> category = CategoryEnum.fromId(product.getCategory());

		if (category.isEmpty()) {
			log.warn("The category id is not valid");
			throw new PantryException(HttpStatus.BAD_REQUEST, "The category is not valid.");
		}

		Product productToSave = new Product();
		productToSave.setAmount(product.getAmount());
		productToSave.setCategory(category.get().toCategory());
		productToSave.setCodeKey(product.getCodeKey());
		productToSave.setName(product.getName());
		productToSave.setPrice(product.getPrice());
		productToSave.setUserId(userId);
		// set id with uuid
		productToSave.setId(generateUUID());

		return db.insertProduct(productToSave);
	}

	/**
	 * Put product.
	 *
	 * @param userId  the user id
	 * @param id      the id
	 * @param product the product
	 * @return the optional
	 * @throws PantryException the error service
	 */
	@Override
	public Optional<Product> update(String userId, String id, ProductDTO product) throws PantryException {
		log.info("update-----userId: {}, id:{} ,product: {}", userId, id, product);

		Product productToUpdate = new Product();
		// the category maybe can updated
		Optional<CategoryEnum> category = CategoryEnum.fromId(product.getCategory());
		if (category.isEmpty()) {
			log.warn("The category id is not valid");
			throw new PantryException(HttpStatus.BAD_REQUEST, "The category is not valid.");
		}
		productToUpdate.setCategory(category.get().toCategory());
		productToUpdate.setAmount(product.getAmount());
		productToUpdate.setCodeKey(product.getCodeKey());
		productToUpdate.setName(product.getName());
		productToUpdate.setPrice(product.getPrice());
		productToUpdate.setUserId(userId);

		return db.updateProduct(userId, id, productToUpdate);
	}

	/**
	 * Adds the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @throws PantryException the error service
	 */
	@Override
	public void addAmount(String userId, String id, int amount) throws PantryException {
		boolean isUpdated = db.addAmountById(userId, id, amount);
		if (!isUpdated) {
			throw new PantryException(HttpStatus.FORBIDDEN, "The id is not valid or you haven't got grants");
		}

	}

	/**
	 * Removes the amount to product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @param amount the amount
	 * @throws PantryException the error service
	 */
	@Override
	public void removeAmount(String userId, String id, int amount) throws PantryException {
		log.info(" ---> ProductService------ removeAmount--userId: {}, id: {}, amount: {}", userId, id, amount);
		boolean isUpdated = db.removeAmountById(userId, id, amount);
		if (!isUpdated) {
			throw new PantryException(HttpStatus.FORBIDDEN, "Can't be updated the amount");
		}

	}

	/**
	 * Delete product.
	 *
	 * @param userId the user id
	 * @param id     the id
	 * @throws PantryException the error service
	 */
	@Override
	public void delete(String userId, String id) throws PantryException {
		log.info(" ---> ProductService------ delete--userId: {}, id: {}", userId, id);
		boolean wasDeleted = db.deleteProduct(userId, id);
		if (!wasDeleted) {
			log.error("delete----THE PRODUCT CAN'T BE DELETED");
			throw new PantryException(HttpStatus.FORBIDDEN, "The product can't be deleted");
		}
	}

	/**
	 * Gets the body for excel.
	 *
	 * @param userId the user id
	 * @return the body for excel
	 * @throws PantryException the error service
	 */
	private String getBodyForExcel(String userId) throws PantryException {
		Optional<List<Product>> products = all(userId);
		if (products.isEmpty() || products.get().isEmpty()) {
			log.warn("The user {} doesn't have products");
			throw new PantryException(HttpStatus.NO_CONTENT,
					String.format("The user %s haven't got any products", userId));
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(products.get());
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage(), e);
			throw new PantryException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	private String getUrl() throws PantryException {
		List<ServiceInstance> services = discoveryClient.getInstances(idFileInstance);
		if (services.isEmpty()) {
			throw new PantryException(HttpStatus.SERVICE_UNAVAILABLE,
					"the service for files is unavailable now, try later O.o!");
		}
		ServiceInstance instanceInfo = services.get(0);
		return String.format(TEMPLATE_URL, instanceInfo.getHost(), instanceInfo.getPort());
	}

}
