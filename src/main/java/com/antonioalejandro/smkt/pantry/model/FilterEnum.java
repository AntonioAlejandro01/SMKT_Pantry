package com.antonioalejandro.smkt.pantry.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * The Enum FilterEnum.
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
	 * @throws ErrorService the error service
	 */
	public static FilterEnum fromName(String name) throws ErrorService {
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
			throw new ErrorService(HttpStatus.BAD_REQUEST, "the filter is not valid");
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
			operationSearch = (userId, value, dao) -> dao.searchByName(userId, value);
			break;
		case AMOUNT:
			operationSearch = (userId, value, dao) -> {
				try {
					return dao.searchByAmount(userId, Integer.parseInt(value));
				} catch (NumberFormatException e) {
					throw new ErrorService(HttpStatus.BAD_REQUEST,
							"The value of value parameter is not valid for amount, the type must be a integer");
				}
			};
			break;
		case CATEGORY:
			operationSearch = (userId, value, dao) -> {
				try {
					return dao.searchByCateogry(userId, Integer.parseInt(value));
				} catch (NumberFormatException e) {
					throw new ErrorService(HttpStatus.BAD_REQUEST,
							"The value of value parameter is not valid for category, the type must be a integer");
				}
			};
			break;
		case PRICE:
			operationSearch = (userId, value, dao) -> {
				try {
					return dao.searchByPrice(userId, Double.parseDouble(value));
				} catch (NumberFormatException e) {
					throw new ErrorService(HttpStatus.BAD_REQUEST,
							"The value of value parameter is not valid for price, the type must be a double");
				}
			};
			break;
		default:// codeKey
			operationSearch = (userId, value, dao) -> dao.searchByCodekey(userId, value);
			break;
		}

		return operationSearch;
	}

}
