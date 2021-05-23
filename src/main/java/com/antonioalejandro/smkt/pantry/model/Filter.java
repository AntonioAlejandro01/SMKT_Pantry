package com.antonioalejandro.smkt.pantry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Filter", description = "The filter value")
public class Filter {

	/** The id. */
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private int id;

	/** The value. */
	@ApiModelProperty(dataType = "string", example = "NAME", position = 1, value = "the value that can be used as key in path /product/search/")
	private String value;
}
