package com.antonioalejandro.utils.smkt.pantry.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.utils.UUIDGenerator;

class UUIDGeneratorTest {

	@Test
	void testNoBlanck() throws Exception {
		assertFalse(new UUIDGenerator() {
		}.generateUUID().isBlank());
	}
}
