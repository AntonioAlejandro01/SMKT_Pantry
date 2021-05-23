package com.antonioalejandro.smkt.pantry.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.antonioalejandro.smkt.pantry.model.Filter;
import com.antonioalejandro.smkt.pantry.model.OperationSearchProduct;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * The Enum Filter
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @apiNote Enum for filter
 */
public enum FilterEnum {

	/** The name. */
	NAME(1),
	/** The category. */
	CATEGORY(2),
	/** The codekey. */
	CODEKEY(3),
	/** The price. */
	PRICE(4),
	/** The amount. */
	AMOUNT(5);

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Getter
	private final int id;

	/**
	 * Instantiates a new filter enum.
	 *
	 * @param id the id
	 */
	private FilterEnum(int id) {
		this.id = id;
	}

	/**
	 * From name.
	 *
	 * @param name the name
	 * @return the filter enum
	 * @throws PantryException the error service
	 */
	public static FilterEnum fromName(String name) throws PantryException {
		FilterEnum filterEnum;

		switch (name.toUpperCase()) {
			case "CATEGORY":
				filterEnum = CATEGORY;
				break;
			case "CODEKEY":
				filterEnum = CODEKEY;
				break;
			case "PRICE":
				filterEnum = PRICE;
				break;
			case "AMOUNT":
				filterEnum = AMOUNT;
				break;
			case "NAME":
				filterEnum = NAME;
				break;
			default:
				throw new PantryException(HttpStatus.BAD_REQUEST, "the filter is not valid");
		}
		return filterEnum;
	}

	/**
	 * All filters.
	 *
	 * @return the list
	 */
	public static List<Filter> allFilters() {
		return Stream.of(FilterEnum.values())
				.map(filterEnum -> Filter.builder().id(filterEnum.id).value(filterEnum.toString()).build())
				.collect(Collectors.toList());
	}

	/**
	 * Gets the function for search.
	 *
	 * @return the function for search
	 */
	public OperationSearchProduct getFunctionForSearch() {
		OperationSearchProduct operationSearch = null;
		switch (this) {
			case NAME:
				operationSearch = (userId, value, db) -> db.findByName(userId, value);
				break;
			case AMOUNT:
				operationSearch = (userId, value, db) -> {
					try {
						return db.findByAmount(userId, Integer.parseInt(value));
					} catch (NumberFormatException e) {
						throw new PantryException(HttpStatus.BAD_REQUEST,
								"The value of value parameter is not valid for amount, the type must be a integer");
					}
				};
				break;
			case CATEGORY:
				operationSearch = (userId, value, db) -> {
					try {
						return db.findByCategory(userId, Integer.parseInt(value));
					} catch (NumberFormatException e) {
						throw new PantryException(HttpStatus.BAD_REQUEST,
								"The value of value parameter is not valid for category, the type must be a integer");
					}
				};
				break;
			case PRICE:
				operationSearch = (userId, value, db) -> {
					try {
						return db.findByPrice(userId, Double.parseDouble(value));
					} catch (NumberFormatException e) {
						throw new PantryException(HttpStatus.BAD_REQUEST,
								"The value of value parameter is not valid for price, the type must be a double");
					}
				};
				break;
			default:// codeKey
				operationSearch = (userId, value, db) -> db.findByCodeKey(userId, value).map(Arrays::asList);
				break;
		}

		return operationSearch;
	}

}
