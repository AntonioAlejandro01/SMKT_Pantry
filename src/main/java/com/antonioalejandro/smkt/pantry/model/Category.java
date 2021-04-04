package com.antonioalejandro.smkt.pantry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class Category {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/** The value. */
	@Column(nullable = false, unique = true)
	private String value;
}
