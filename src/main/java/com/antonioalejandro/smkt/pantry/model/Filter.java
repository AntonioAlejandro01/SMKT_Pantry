package com.antonioalejandro.smkt.pantry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Builder
public class Filter {
	
	/** The id. */
	@JsonIgnore
	private int id;

	/** The value. */
	private String value;
}
