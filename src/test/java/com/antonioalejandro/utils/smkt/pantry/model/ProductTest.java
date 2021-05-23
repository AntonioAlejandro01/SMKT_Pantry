package com.antonioalejandro.utils.smkt.pantry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.Product;

class ProductTest {

	@Test
	void testConstructorAndStaticField() throws Exception {
		assertNotNull(new Product());
		assertFalse(new Product().toString().isBlank());
	}

	@Test
	void testSetters() throws Exception {
		var product = new Product();

		product.setAmount(1);
		product.setCategory(new Category(1,"FOOD"));
		product.setCodeKey("CODEKEY");
		product.setId("ID");
		product.setName("NAME");
		product.setPrice(0.23d);
		product.setUserId("USERID");

		assertEquals(1, product.getAmount());
		assertEquals(1,product.getCategory().getId());
		assertEquals("CODEKEY", product.getCodeKey());
		assertEquals("ID", product.getId());
		assertEquals("NAME", product.getName());
		assertEquals(0.23d, product.getPrice());
		assertEquals("USERID", product.getUserId());
		
		assertEquals(1, product.category());
	}

}
