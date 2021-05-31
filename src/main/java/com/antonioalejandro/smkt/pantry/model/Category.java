package com.antonioalejandro.smkt.pantry.model;

import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Category.
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @apiNote Category for products
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "Category", description = "The category")
public class Category {

	/** The id. */
	@ApiModelProperty(dataType = "integer", position = 1, example = "1", value = "The ID of Category")
	@Id
	private int id;

	/** The value. */
	@ApiModelProperty(dataType = "string", position = 2, example = "FOOD", value = "The Name of Category")
	private String value;
}
