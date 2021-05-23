package com.antonioalejandro.utils.smkt.pantry.db;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.bson.BsonString;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.antonioalejandro.smkt.pantry.db.PantryDatabaseImpl;
import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

class PantryDatabaseTest {

	private PantryDatabaseImpl db;

	private MongoCollection<Document> collection;

	@BeforeEach
	void init() {
		collection = mock(MongoCollection.class);
	}

	@Test
	void testContructorException() throws Exception {
		assertThrows(Exception.class, () -> new PantryDatabaseImpl("", "", ""));
	}

	@Test
	void testfindAll() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(cursor.next()).thenReturn(createDocumentOfProduct("Product Test 1"))
				.thenReturn(createDocumentOfProduct("Product Test 2"));

		verifyList(db.findAll("Admin"));
	}

	@Test
	void testfindAllEmpty() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(false);

		assertTrue(db.findAll("Admin").isEmpty());
	}

	@Test
	void testFindById() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);

		FindIterable<Document> iterable = mock(FindIterable.class);
		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		verifyProduct(db.findById("Admin", "ID"));
	}

	@Test
	void testfindByName() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);
		MongoCursor<Document> cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);

		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(cursor.next()).thenReturn(createDocumentOfProduct("Product Test 1"))
				.thenReturn(createDocumentOfProduct("Product Test 2"));

		verifyList(db.findByName("USERID", "NAME"));

	}

	@Test
	void testFindByCodekey() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);

		var iterable = mock(FindIterable.class);
		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		verifyProduct(db.findByCodeKey("Admin", "CODEKEY"));
	}

	@Test
	void testFindByPrice() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);

		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(cursor.next()).thenReturn(createDocumentOfProduct("Product Test 1"))
				.thenReturn(createDocumentOfProduct("Product Test 2"));

		verifyList(db.findByPrice("USERID", 1.23d));

	}

	@Test
	void testFindByCategory() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);

		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(cursor.next()).thenReturn(createDocumentOfProduct("Product Test 1"))
				.thenReturn(createDocumentOfProduct("Product Test 2"));

		verifyList(db.findByCategory("USERID", 1));

	}

	@Test
	void testFindByAmount() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var cursor = mock(MongoCursor.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		Mockito.doCallRealMethod().when(iterable).forEach(Mockito.any(Consumer.class));

		when(iterable.iterator()).thenReturn(cursor);

		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);

		when(cursor.next()).thenReturn(createDocumentOfProduct("Product Test 1"))
				.thenReturn(createDocumentOfProduct("Product Test 2"));

		verifyList(db.findByAmount("USERID", 1));

	}

	@Test
	void testAddAmountByIdEmptyProduct() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(null);

		assertThrows(PantryException.class, () -> db.addAmountById("USERID", "ID", 1));

	}

	@Test
	void testAddAmountByIdNotModified() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var result = mock(UpdateResult.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));
		when(result.getModifiedCount()).thenReturn(0L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);

		assertFalse(db.addAmountById("USERID", "ID", 0));

	}

	@Test
	void testAddAmountByIdModified() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var result = mock(UpdateResult.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));
		when(result.getModifiedCount()).thenReturn(1L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);

		assertTrue(db.addAmountById("USERID", "ID", 0));

	}

	/////

	@Test
	void testRemoveAmountByIdEmptyProduct() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		FindIterable<Document> iterable = mock(FindIterable.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(null);

		assertThrows(PantryException.class, () -> db.removeAmountById("USERID", "ID", 1));

	}

	@Test
	void testRemoveAmountByIdNotModified() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var result = mock(UpdateResult.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));
		when(result.getModifiedCount()).thenReturn(0L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);

		assertFalse(db.removeAmountById("USERID", "ID", 0));

	}

	@Test
	void testRemoveAmountByIdModified() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var result = mock(UpdateResult.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));
		when(result.getModifiedCount()).thenReturn(1L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);

		assertTrue(db.removeAmountById("USERID", "ID", 0));

	}

	@Test
	void testRemoveAmountByIdModifiedNegativeAmount() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var iterable = mock(FindIterable.class);
		var result = mock(UpdateResult.class);

		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);

		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));
		when(result.getModifiedCount()).thenReturn(1L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);

		assertThrows(PantryException.class, () -> db.removeAmountById("USERID", "ID", 2));

	}

	@Test
	void testInserProductError() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var result = mock(InsertOneResult.class);
		when(result.getInsertedId()).thenReturn(null);
		when(collection.insertOne(Mockito.any(Document.class))).thenReturn(result);

		assertFalse(db.insertProduct(createProduct()).isPresent());

	}

	@Test
	void testInserProduct() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var result = mock(InsertOneResult.class);
		var iterable = mock(FindIterable.class);
		var resultId = mock(BsonString.class);
		when(resultId.asString()).thenReturn(new BsonString("ID"));
		when(result.getInsertedId()).thenReturn(resultId);
		when(collection.insertOne(Mockito.any(Document.class))).thenReturn(result);
		when(collection.find(Mockito.any(Document.class))).thenReturn(iterable);
		when(iterable.first()).thenReturn(createDocumentOfProduct("Product Test 1"));

		verifyProduct(db.insertProduct(createProduct()));
	}

	@Test
	void testDeleteProduct() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var result = mock(DeleteResult.class);
		when(result.getDeletedCount()).thenReturn(1L);
		when(collection.deleteOne(Mockito.any(Document.class))).thenReturn(result);

		assertTrue(db.deleteProduct("USERID", "ID"));
	}

	@Test
	void testUpdateProduct() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var result = mock(UpdateResult.class);
		var resultFind = mock(FindIterable.class);
		when(resultFind.first()).thenReturn(createDocumentOfProduct("Product Test 1"));
		when(result.getModifiedCount()).thenReturn(1L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);
		when(collection.find(Mockito.any(Document.class))).thenReturn(resultFind);
		verifyProduct(db.updateProduct("USERID", "ID", createProduct()));
	}

	@Test
	void testUpdateProductError() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var result = mock(UpdateResult.class);
		when(result.getModifiedCount()).thenReturn(0L);
		when(collection.updateOne(Mockito.any(Document.class), Mockito.any(Document.class))).thenReturn(result);
		assertThrows(PantryException.class, () -> db.updateProduct("USERID", "ID", createProduct()));
	}

	@Test
	void testDeleteProductNotDeleted() throws Exception {
		db = new PantryDatabaseImpl("mongodb://root:secret@localhost:27017/", "smkt", "recipes");
		ReflectionTestUtils.setField(db, "collection", collection);
		var result = mock(DeleteResult.class);
		when(result.getDeletedCount()).thenReturn(0L);
		when(collection.deleteOne(Mockito.any(Document.class))).thenReturn(result);

		assertFalse(db.deleteProduct("USERID", "ID"));
	}
	// ---- UTILS FOR TESTING

	private Document createDocumentOfProduct(String name) {
		return new Document("_id", UUID.randomUUID().toString()).append("name", name).append("amount", 1)
				.append("codeKey", UUID.randomUUID().toString())
				.append("category", new Document("_id", 1).append("value", "FOOD"));
	}

	private void verifyList(Optional<List<Product>> products) {
		assertTrue(products.isPresent());
		assertFalse(products.get().isEmpty());

		List<Product> p = products.get();

		assertEquals(2, p.size());
		assertEquals("Product Test 1", p.get(0).getName());
		assertEquals("Product Test 2", p.get(1).getName());
	}

	private void verifyProduct(Optional<Product> product) {
		assertTrue(product.isPresent());
		assertEquals("Product Test 1", product.get().getName());
	}

	private Product createProduct() {
		var p = new Product();
		p.setId(UUID.randomUUID().toString());
		p.setAmount(1);
		p.setCategory(new Category(1, "FOOD"));
		p.setCodeKey(UUID.randomUUID().toString());
		p.setName("Product test 1");
		p.setPrice(1d);
		p.setUserId("ADMIN");
		return p;
	}

}
