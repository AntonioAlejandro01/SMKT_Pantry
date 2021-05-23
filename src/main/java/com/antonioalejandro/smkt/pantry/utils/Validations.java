package com.antonioalejandro.smkt.pantry.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;

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
	 * @throws PantryException the error service
	 */
	public void id(String id) throws PantryException {
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
	 * @throws PantryException the error service
	 */
	public void product(ProductDTO product) throws PantryException {
		if (product == null) {
			throw mandatoryError("product");
		}
		amount(product.getAmount());
		if (product.getCategory() == 0) {
			throw new PantryException(HttpStatus.BAD_REQUEST, "Category not valid");
		}
		string("name", product.getName());
		string("codeKey",product.getCodeKey());
		if ( product.getPrice() == null || Double.toString(product.getPrice()).replace("0", "").replace(".", "").isBlank()) {
			throw new PantryException(HttpStatus.BAD_REQUEST, "The price is not valid");
		}
		

	}
	
	/**
	 * Value.
	 *
	 * @param value the value
	 * @throws PantryException the error service
	 */
	public void value(String value) throws PantryException {
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
	 * @throws PantryException the error service
	 */
	public void amount(int amount) throws PantryException{
		if (amount <= 0) {
			throw negativeOrZeroAmountError("amount");
		}
		
	}
	
	/**
	 * String.
	 *
	 * @param field the field
	 * @param value the value
	 * @throws PantryException the error service
	 */
	public void string(String field,String value) throws PantryException {
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
	private PantryException negativeOrZeroAmountError(String field) {
		return new PantryException(HttpStatus.BAD_REQUEST, String.format("the %s can't be zero or less tha 0", field));
	}
	
	/**
	 * Mandatory error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private PantryException mandatoryError(String field) {
		return new PantryException(HttpStatus.BAD_REQUEST, String.format("The %s is mandatory", field));
	}
	
	/**
	 * Empty error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private PantryException emptyError(String field) {
		return new PantryException(HttpStatus.BAD_REQUEST, String.format("The %s can't be empty", field));
	}

}
