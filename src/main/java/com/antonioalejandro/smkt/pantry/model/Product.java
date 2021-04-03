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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
@JsonIgnoreProperties({ "userId" })
public class Product{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** The name. */
	private String name;

	/** The category. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Category category;

	/** The code key. */
	private String codeKey;

	/** The price. */
	private Double price;

	/** The amount. */
	private Integer amount;

	/** The user id. */
	private String userId;
	
	@JsonGetter()
	public int category() {
		return this.category.getId();
	}
	

}
