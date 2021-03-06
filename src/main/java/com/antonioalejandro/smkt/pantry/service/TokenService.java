package com.antonioalejandro.smkt.pantry.service;

import java.util.Optional;


/**
 * The Interface TokenService.
 */
public interface TokenService {

	/**
	 * Gets the user id.
	 *
	 * @param token the token
	 * @return the user id
	 */
	public Optional<String> getUserId(String token);
}
