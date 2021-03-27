/*
 * @Author AntonioAlejandro01
 * 
 * @link http://antonioalejandro.com
 * @link https://github.com/AntonioAlejandro01/SMKT_Pantry
 * 
 */
package com.antonioalejandro.smkt.pantry.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private int id;

	/** The name. */
	@JsonProperty
	private String name;

	/** The category. */
	@JsonProperty
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Category category;

	/** The code key. */
	@JsonProperty
	private String codeKey;

	/** The price. */
	@JsonProperty
	private Double price;

	/** The amount. */
	@JsonProperty
	private Integer amount;

}
