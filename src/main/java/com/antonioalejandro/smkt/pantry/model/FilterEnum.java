package com.antonioalejandro.smkt.pantry.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum FilterEnum {
	NAME(1), CATEGORY(2), CODEKEY(3), PRICE(4), AMOUNT(5);

	@Getter
	private final int id;

	private FilterEnum(int id) {
		this.id = id;
	}

	public static FilterEnum fromName(String name) throws ErrorService{
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

	public static List<Filter> allFilters() {
		return Stream.of(FilterEnum.values())
				.map(filterEnum -> Filter.builder().id(filterEnum.id).value(filterEnum.toString()).build())
				.collect(Collectors.toList());
	}

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
							"The value of value parameter is not valid for amount the type must be a integer");
				}
			};
			break;
		case CATEGORY:
			operationSearch = (userId, value, dao) -> dao.searchByCateogry(userId, Integer.parseInt(value));
			break;
		case PRICE:
			operationSearch = (userId, value, dao) -> {
				try {
					return dao.searchByPrice(userId, Double.parseDouble(value));
				} catch (NumberFormatException e) {
					throw new ErrorService(HttpStatus.BAD_REQUEST,
							"The value of value parameter is not valid for amount the type must be a double");
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
