package com.antonioalejandro.utils.smkt.pantry.model.enums;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.enums.FilterEnum;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.repository.PantryRepository;

class FilterEnumTest {

	@Test
	void testGetAndFromNameAndAllFilters() throws Exception {
		assertEquals(1, FilterEnum.NAME.getId());
		assertEquals(2, FilterEnum.CATEGORY.getId());
		assertEquals(3, FilterEnum.CODEKEY.getId());
		assertEquals(4, FilterEnum.PRICE.getId());
		assertEquals(5, FilterEnum.AMOUNT.getId());

		assertEquals(FilterEnum.NAME, FilterEnum.fromName("NAME"));
		assertEquals(FilterEnum.CATEGORY, FilterEnum.fromName("CATEGORY"));
		assertEquals(FilterEnum.CODEKEY, FilterEnum.fromName("CODEKEY"));
		assertEquals(FilterEnum.PRICE, FilterEnum.fromName("PRICE"));
		assertEquals(FilterEnum.AMOUNT, FilterEnum.fromName("AMOUNT"));

		assertThrows(PantryException.class, () -> FilterEnum.fromName("X"));

		assertFalse(FilterEnum.allFilters().isEmpty());
	}

	@Test
	void testGetFunctionSearch() throws Exception {
		var db = mock(PantryRepository.class);

		assertDoesNotThrow(() -> {
			FilterEnum.NAME.getFunctionForSearch().search("", "", db);
		});
		assertDoesNotThrow(() -> {
			FilterEnum.CODEKEY.getFunctionForSearch().search("", "", db);
		});
		assertDoesNotThrow(() -> {
			FilterEnum.AMOUNT.getFunctionForSearch().search("", "1", db);
		});
		assertThrows(PantryException.class, () -> {
			FilterEnum.AMOUNT.getFunctionForSearch().search("", "", db);
		});
		assertDoesNotThrow(() -> {
			FilterEnum.CATEGORY.getFunctionForSearch().search("", "1", db);
		});
		assertThrows(PantryException.class, () -> {
			FilterEnum.CATEGORY.getFunctionForSearch().search("", "", db);
		});
		assertDoesNotThrow(() -> {
			FilterEnum.PRICE.getFunctionForSearch().search("", "1.23", db);
		});
		assertThrows(PantryException.class, () -> {
			FilterEnum.PRICE.getFunctionForSearch().search("", "", db);
		});

	}
}
