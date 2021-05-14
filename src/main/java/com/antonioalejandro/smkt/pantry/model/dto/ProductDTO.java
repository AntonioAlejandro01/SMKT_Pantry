package com.antonioalejandro.smkt.pantry.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Product DTO
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @apiNote Class prepare to send in JSON
 */
@Data
@ApiModel(value = "Product Request", description = "Product for request, not include the ID")
public class ProductDTO {

	/** The name. */
	@ApiModelProperty(dataType = "string", example = "Chips", position = 1, value = "the name for product", allowEmptyValue = false)
	private String name;

	/** The category. */
	@ApiModelProperty(dataType = "integer", example = "1", position = 2, value = "the id for Category", allowableValues = "1, 2, 3, 4, 5", allowEmptyValue = false)
	private int category;

	/** The code key. */
	@ApiModelProperty(dataType = "string", example = "234daec234vafd", position = 3, value = "the key for product(barcode)", allowEmptyValue = false)
	private String codeKey;

	/** The price. */
	@ApiModelProperty(dataType = "double", example = "2.23", position = 4, value = "the price for the product", allowEmptyValue = false)
	private Double price;

	/** The amount. */
	@ApiModelProperty(dataType = "integer", example = "1", position = 5, value = "The amount for product", allowEmptyValue = false)
	private Integer amount;
}
