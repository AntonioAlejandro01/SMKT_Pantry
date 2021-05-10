package com.antonioalejandro.smkt.pantry.utils;

import java.util.UUID;

/**
 * The Class Utils.
 */
public interface UUIDGenerator {

	/**
	 * Generate UUID.
	 *
	 * @return the string
	 */
	default String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
