package com.antonioalejandro.utils.smkt.pantry.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.Filter;

class FilterTest {

	@Test
	void testConstructors() throws Exception {
		assertNotNull(new Filter());
		assertNotNull(new Filter(1, "FILTER"));
		assertNotNull(Filter.builder().id(1).value("VALUE").build());
		assertFalse(Filter.builder().toString().isBlank());
		assertFalse(new Filter().toString().isBlank());
	}

	@Test
	void testSetters() throws Exception {
		var filter = new Filter();

		filter.setId(1);
		filter.setValue("VALUE");

		assertEquals(1, filter.getId());
		assertEquals("VALUE", filter.getValue());
	}
}
