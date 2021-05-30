package com.antonioalejandro.smkt.pantry.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Product Class
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({ "userId" })
@ApiModel(description = "Product for response , include de ID", value = "Product Response")
public class Product {

	/** The id. */
	@ApiModelProperty(dataType = "string", example = "123e4567-e89b-42d3-a456-556642440000", position = 0, value = "The ID for Product, it's a UUID")
	private String id;

	/** The name. */
	@ApiModelProperty(dataType = "string", example = "Chips", position = 1, value = "the name for product")
	private String name;

	/** The category. */
	private Category category;

	/** The code key. */
	@ApiModelProperty(dataType = "string", example = "234daec234vafd", position = 3, value = "the key for product(barcode)")
	private String codeKey;

	/** The price. */
	@ApiModelProperty(dataType = "double", example = "2.23", position = 4, value = "the price for the product")
	private Double price;

	/** The amount. */
	@ApiModelProperty(dataType = "integer", example = "1", position = 5, value = "The amount for product")
	private Integer amount;

	/** The user id. */
	@ApiModelProperty(hidden = true)
	private String userId;

	/**
	 * Category.
	 *
	 * @return the int
	 */
	@JsonGetter()
	@ApiModelProperty(dataType = "integer", example = "1", position = 2, value = "the id for Category", allowableValues = "1, 2, 3, 4, 5")
	public int category() {
		return this.category.getId();
	}

}
