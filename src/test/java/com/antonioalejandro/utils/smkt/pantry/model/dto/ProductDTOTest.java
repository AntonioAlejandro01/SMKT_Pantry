package com.antonioalejandro.utils.smkt.pantry.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;

class ProductDTOTest {

	@Test
	void testConstructorAndStaticField() throws Exception {
		assertNotNull(new ProductDTO());
		assertFalse(new ProductDTO().toString().isBlank());

	}

	@Test
	void testSetters() throws Exception {
		var product = new ProductDTO();

		product.setAmount(1);
		product.setCategory(1);
		product.setCodeKey("CODEKEY");
		product.setName("NAME");
		product.setPrice(0.23d);

		assertEquals(1, product.getAmount());
		assertEquals(1, product.getCategory());
		assertEquals("CODEKEY", product.getCodeKey());
		assertEquals("NAME", product.getName());
		assertEquals(0.23d, product.getPrice());

	}

}
