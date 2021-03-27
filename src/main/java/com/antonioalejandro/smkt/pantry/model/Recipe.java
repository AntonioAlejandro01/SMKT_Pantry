/*
 * @Author AntonioAlejandro01
 * 
 * @link http://antonioalejandro.com
 * @link https://github.com/AntonioAlejandro01/SMKT_Users
 * 
 */
package com.antonioalejandro.smkt.pantry.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Recipe.
 */
@Getter
@Setter
@ToString
public class Recipe {

	/** The id. */
	@JsonProperty
	private String id;

	/** The title. */
	@JsonProperty
	private String title;

	/** The ingredients. */
	@JsonProperty
	private List<Ingredient> ingredients;

	/** The steps. */
	@JsonProperty
	private List<String> steps;

	/** The time. */
	@JsonProperty
	private Integer time;

}
