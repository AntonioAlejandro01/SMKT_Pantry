package com.antonioalejandro.smkt.pantry.utils;

import java.util.UUID;

/**
 * The Class Utils.
 */
public class Utils {

	/**
	 * Instantiates a new utils.
	 */
	private Utils() {
		
	}
	
	/**
	 * Generate UUID.
	 *
	 * @return the string
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
