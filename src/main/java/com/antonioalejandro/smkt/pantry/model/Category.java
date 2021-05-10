package com.antonioalejandro.smkt.pantry.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class Category.
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @apiNote Category for products
 */
@Data
@ApiModel(value = "Category", description = "The category")
public class Category {

	/** The id. */
	@ApiModelProperty(dataType = "integer", position = 1, example = "1", value = "The ID of Category")
	private int id;

	/** The value. */
	@ApiModelProperty(dataType = "string", position = 2, example = "FOOD", value = "The Name of Category")
	private String value;
}
