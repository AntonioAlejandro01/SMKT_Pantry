package com.antonioalejandro.utils.smkt.pantry.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.utils.Mappers;

class MappersTest {

	@Test
	void testProductToDocument() throws Exception {
		assertNotNull(new Mappers() {
		}.productToDocument(createProduct()));
	}

	@Test
	void testDocumentToProduct() throws Exception {
		assertNotNull(new Mappers() {
		}.documentToProduct(createDocument()));
	}

	private Product createProduct() {
		var product = new Product();
		product.setAmount(1);
		product.setCategory(new Category(1, "X"));
		product.setCodeKey("X");
		product.setId("ID");
		product.setName("NAME");
		product.setPrice(2.3d);
		product.setUserId("USERID");

		return product;
	}

	private Document createDocument() {
		return new Document("_id", "ID").append("amount", 12).append("codeKey", "X").append("name", "X")
				.append("price", 2.3d).append("userId", "Admin")
				.append("category", new Document("_id", 1).append("value", "FOOD"));
	}
}
