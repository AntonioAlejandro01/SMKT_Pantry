package com.antonioalejandro.smkt.pantry.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.exceptions.ErrorService;

/**
 * The Class Validations.
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @apiNote Class for validations in controller
 */
@Component
public class Validations {

	/**
	 * Id.
	 *
	 * @param id the id
	 * @throws ErrorService the error service
	 */
	public void id(String id) throws ErrorService {
		if (id == null) {
			throw mandatoryError("id");
		}

		if (id.isBlank()) {
			throw emptyError("id");
		}

	}
	
	/**
	 * Product
	 *
	 * @param product the product
	 * @throws ErrorService the error service
	 */
	public void product(ProductDTO product) throws ErrorService {
		if (product == null) {
			throw mandatoryError("product");
		}
		amount(product.getAmount());
		if (product.getCategory() == 0) {
			throw new ErrorService(HttpStatus.BAD_REQUEST, "Category not valid");
		}
		string("name", product.getName());
		string("codeKey",product.getCodeKey());
		if ( product.getPrice() == null || Double.toString(product.getPrice()).replace("0", "").isBlank()) {
			throw new ErrorService(HttpStatus.BAD_REQUEST, "The price is not valid");
		}
		

	}
	
	/**
	 * Value.
	 *
	 * @param value the value
	 * @throws ErrorService the error service
	 */
	public void value(String value) throws ErrorService {
		if (value == null) {
			throw mandatoryError("value");
		}
		if (value.isBlank()) {
			throw emptyError("value");
		}
	}
	
	/**
	 * Amount.
	 *
	 * @param amount the amount
	 * @throws ErrorService the error service
	 */
	public void amount(int amount) throws ErrorService{
		if (amount <= 0) {
			throw negativeOrZeroAmountError("amount");
		}
		
	}
	
	/**
	 * String.
	 *
	 * @param field the field
	 * @param value the value
	 * @throws ErrorService the error service
	 */
	public void string(String field,String value) throws ErrorService {
		if (value == null) {
			throw mandatoryError(field);
		}

		if (value.isBlank()) {
			throw emptyError(field);
		}

	}
	
	
	/**
	 * Negative or zero amount error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ErrorService negativeOrZeroAmountError(String field) {
		return new ErrorService(HttpStatus.BAD_REQUEST, String.format("the %s can't be zero or less tha 0", field));
	}
	
	/**
	 * Mandatory error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ErrorService mandatoryError(String field) {
		return new ErrorService(HttpStatus.BAD_REQUEST, String.format("The %s is mandatory", field));
	}
	
	/**
	 * Empty error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ErrorService emptyError(String field) {
		return new ErrorService(HttpStatus.BAD_REQUEST, String.format("The %s can't be empty", field));
	}

}
