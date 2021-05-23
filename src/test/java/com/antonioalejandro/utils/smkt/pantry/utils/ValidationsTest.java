package com.antonioalejandro.utils.smkt.pantry.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.utils.Validations;

class ValidationsTest {

	@Test
	void testIdAndValueAndString() throws Exception {
		var validations = new Validations();
		assertThrows(PantryException.class, () -> validations.id(null));
		assertThrows(PantryException.class, () -> validations.id(""));
		assertDoesNotThrow(() -> validations.id("X"));

		assertThrows(PantryException.class, () -> validations.string("", null));
		assertThrows(PantryException.class, () -> validations.string("", ""));
		assertDoesNotThrow(() -> validations.string("X", "X"));

		assertThrows(PantryException.class, () -> validations.value(null));
		assertThrows(PantryException.class, () -> validations.value(""));
		assertDoesNotThrow(() -> validations.value("X"));
	}

	@Test
	void testAmountAndProduct() throws Exception {
		var validations = new Validations();
		assertThrows(PantryException.class, () -> validations.amount(0));
		assertThrows(PantryException.class, () -> validations.amount(-1));
		assertDoesNotThrow(() -> validations.amount(1));

		var product = new ProductDTO();

		product.setAmount(1);
		product.setName("NAME");
		product.setCodeKey("CODEKEY");

		product.setCategory(0);

		assertThrows(PantryException.class, () -> validations.product(null));
		assertThrows(PantryException.class, () -> validations.product(product));
		product.setCategory(1);
		product.setPrice(null);
		assertThrows(PantryException.class, () -> validations.product(product));
		product.setPrice(0d);
		assertThrows(PantryException.class, () -> validations.product(product));
		product.setPrice(2.3d);
		assertDoesNotThrow(() -> validations.product(product));
	}
}
