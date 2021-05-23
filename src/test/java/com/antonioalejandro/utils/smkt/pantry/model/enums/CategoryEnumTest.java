package com.antonioalejandro.utils.smkt.pantry.model.enums;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.enums.CategoryEnum;

class CategoryEnumTest {
	@Test
	void testToCategory() throws Exception {
		var c = CategoryEnum.FOOD.toCategory();
		assertEquals(1, c.getId());
		assertEquals("FOOD", c.getValue());
		c = CategoryEnum.KITCHENWARE.toCategory();
		assertEquals(2, c.getId());
		assertEquals("KITCHENWARE", c.getValue());
		c = CategoryEnum.CLEANING.toCategory();
		assertEquals(3, c.getId());
		assertEquals("CLEANING", c.getValue());
		c = CategoryEnum.CLEANING_UTILS.toCategory();
		assertEquals(4, c.getId());
		assertEquals("CLEANING_UTILS", c.getValue());
		c = CategoryEnum.OTHERS.toCategory();
		assertEquals(5, c.getId());
		assertEquals("OTHERS", c.getValue());
	}

	@Test
	void testFromId() throws Exception {
		for (int idTest : List.of(0, 1, 2, 3, 4, 5, 6)) {
			if (idTest == 6 || idTest == 0) {
				assertTrue(CategoryEnum.fromId(idTest).isEmpty());
			} else {
				assertTrue(CategoryEnum.fromId(idTest).isPresent());
			}
		}
	}
}
