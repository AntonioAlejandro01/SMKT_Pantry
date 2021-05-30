package com.antonioalejandro.smkt.pantry.utils;

import java.util.UUID;

/**
 * UUID Generator Interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
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
