package com.antonioalejandro.utils.smkt.pantry.config;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.config.SwaggerConfig;

class SwaggerConfigTest {

	@Test
	void testNotNull() throws Exception {
		var config = new SwaggerConfig();
		assertNotNull(config);
		assertNotNull(config.usersApi());
	}
}
