/*
 * @Author AntonioAlejandro01
 * 
 * @link http://antonioalejandro.com
 * @link https://github.com/AntonioAlejandro01/SMKT_Users
 * 
 */
package com.antonioalejandro.smkt.pantry.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Ingredient.
 */
@Getter
@Setter
@ToString
public class Ingredient {

	/** The name. */
	@JsonProperty
	private String name;
	@JsonProperty
	/** The amount. */
	private String amount;

}