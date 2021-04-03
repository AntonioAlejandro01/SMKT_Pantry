package com.antonioalejandro.smkt.pantry.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Class TokenData.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The username. */
	@JsonProperty("user_name")
	private String username;

	/** The scope. */
	private List<String> scope;

	/** The name. */
	private String name;

	/** The active. */
	private boolean active;

	/** The exp. */
	private Long exp;

	/** The authorities. */
	private List<String> authorities;

	/** The jti. */
	private String jti;

	/** The email. */
	private String email;

	/** The client id. */
	@JsonProperty("client_id")
	private String clientId;

	/** The lastname. */
	private String lastname;

	/** The username C. */
	@JsonProperty("username")
	private String usernameC;

}
