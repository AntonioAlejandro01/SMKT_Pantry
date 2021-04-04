package com.antonioalejandro.smkt.pantry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Class Category.
 */
@Entity
@Table(name = "categories")

/**
 * Instantiates a new category.
 */
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer" })
@ApiModel(value = "Category", description = "The category")
public class Category {

	/** The id. */
	@Id
	@ApiModelProperty(dataType = "integer",position = 1,example = "1", value = "The ID of Category")
	private int id;

	/** The value. */
	@Column(nullable = false, unique = true)
	@ApiModelProperty(dataType = "string", position = 2, example = "FOOD", value = "The Name of Category")
	private String value;
}
