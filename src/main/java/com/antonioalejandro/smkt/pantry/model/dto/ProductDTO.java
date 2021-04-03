package com.antonioalejandro.smkt.pantry.model.dto;

import lombok.Data;

/**
 * Instantiates a new product DTO.
 */
@Data
public class ProductDTO {

	/** The name. */
	private String name;

	/** The category. */
	private int category;

	/** The code key. */
	private String codeKey;

	/** The price. */
	private Double price;

	/** The amount. */
	private Integer amount;
}
