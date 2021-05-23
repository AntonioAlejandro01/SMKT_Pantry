package com.antonioalejandro.utils.smkt.pantry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.Category;

class CategoryTest {
	@Test
	void testConstructorEmptyAndToString() throws Exception {
		assertNotNull(new Category());
		assertNotNull(new Category(1,"VALUE"));
		assertFalse(new Category().toString().isBlank());
	}

	@Test
	void testSetters() throws Exception {
		var category = new Category();

		category.setId(1);
		category.setValue("VALUE");
		
		assertEquals(1, category.getId());
		assertEquals("VALUE", category.getValue());

	}
}
