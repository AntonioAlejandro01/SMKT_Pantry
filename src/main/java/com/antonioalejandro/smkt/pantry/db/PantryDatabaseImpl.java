package com.antonioalejandro.smkt.pantry.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.utils.Constants;
import com.antonioalejandro.smkt.pantry.utils.Mappers;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for Pantry Database interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @see Mappers
 * @see PantryDatabase
 * @apiNote Class that manage product database
 */
@Slf4j
@Service
public class PantryDatabaseImpl implements PantryDatabase, Mappers {

	/** the Collection for make operations */
	private MongoCollection<Document> collection;

	/** The create consumer for mongo iterator to list. */
	private Function<List<Product>, Consumer<Document>> createConsumerForMongoIteratorToList;

	/**
	 * Create a MongoDB instance.
	 */
	public PantryDatabaseImpl(@Value("${mongodb.connection}") String connectionString,
			@Value("${mongodb.database.name}") String databaseName,
			@Value("${mongodb.database.collection}") String databaseCollection) {

		log.info("Create Database connection and try connect");
		try (var client = MongoClients.create(connectionString)) {
			MongoDatabase db = client.getDatabase(databaseName);
			log.info("Create or access to collection into database");
			collection = db.getCollection(databaseCollection);
		} catch (Exception e) {
			log.error("ERROR WHEN CREATING DATABASE CLIENT");
			log.debug("ERROR: {}", e);
			throw e;
		}

		// Functions
		this.createConsumerForMongoIteratorToList = products -> doc -> products.add(this.documentToProduct(doc));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<Product>> findAll(String userId) {
		log.info("--> PantryDatabase. findAll. UserId: {}", userId);
		return evaluateList(findList(defaultDocument(userId)));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Product> findById(String userId, String id) {
		log.info("--> ---PantryDatabase---findById----. UserId: {}, idProduct: {}", userId, id);
		return Optional.ofNullable(collection.find(defaultDocument(userId).append(Constants.KEY_ID, id)).first())
				.map(this::documentToProduct);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<Product>> findByName(String userId, String name) {
		log.info("--> ---PantryDatabase---findByName----. UserId: {}, name: {}", userId, name);
		var query = defaultDocument(userId).append(Constants.NAME,
				new Document("$regex", name).append("$options", "i"));
		log.debug("FindbyName----QUERY: {}", query.toJson());
		return evaluateList(findList(query));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Product> findByCodeKey(String userId, String codeKey) {
		log.info("--> ---PantryDatabase---findByCodeKey----. UserId: {}, codeKey: {}", userId, codeKey);
		return Optional.ofNullable(collection.find(defaultDocument(userId).append(Constants.CODE_KEY, codeKey)).first())
				.map(this::documentToProduct);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<Product>> findByPrice(String userId, double price) {
		log.info("--> ---PantryDatabase---findByPrice----. UserId: {}, price: {}", price);
		var query = defaultDocument(userId).append(Constants.PRICE, new Document().append("$lte", price));
		log.debug("findByPrice---QUERY: {}", query.toJson());
		return evaluateList(findList(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<Product>> findByCategory(String userId, int categoryId) {
		log.info("--> ---PantryDatabase---findByCategory----. UserId: {}, categoryId: {}");
		var query = defaultDocument(userId).append(String.format("%s.%s", Constants.CATEGORY, Constants.KEY_ID),
				categoryId);
		log.debug("findByCategory-----QUERY: {}", query.toJson());
		return evaluateList(findList(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<List<Product>> findByAmount(String userId, int amount) {
		log.info("--> ---PantryDatabase---findBy----. UserId: {}, amount: {}", userId, amount);
		var query = defaultDocument(userId).append(Constants.AMOUNT, new Document("$lte", amount));
		log.debug("findByAmount-----QUERY: {}", query.toJson());
		return evaluateList(findList(query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAmountById(String userId, String id, int amount) throws PantryException {
		log.info("--> ---PantryDatabase---addAmountById----. UserId: {}, idProduct: {}, amount: {}", userId, id,
				amount);
		Optional<Product> product = findById(userId, id);
		if (product.isEmpty()) {
			log.error("---PantryDatabase---addAmountById----THE ID IS NOT VALID");
			throw new PantryException(HttpStatus.NOT_FOUND, "The id is not valid.");
		}
		var query = defaultDocument(userId).append(Constants.KEY_ID, id);
		log.debug("addAmountById----QUERY: {}", query.toJson());
		var dataToUpdate = new Document("$inc", new Document(Constants.AMOUNT, amount));
		log.debug("addAmountById----Update: {}", dataToUpdate.toJson());
		return collection.updateOne(query, dataToUpdate).getModifiedCount() > 0;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws PantryException
	 */
	@Override
	public boolean removeAmountById(String userId, String id, int amount) throws PantryException {
		log.info("--> ---PantryDatabase---removeAmountById----. UserId: {}, idProduct: {}, amount: {}", userId, id,
				amount);
		Optional<Product> product = findById(userId, id);
		if (product.isEmpty()) {
			log.error("---PantryDatabase---removeAmountById----THE ID IS NOT VALID");
			throw new PantryException(HttpStatus.NOT_FOUND, "The id is not valid.");

		}
		if (product.get().getAmount() - amount < 0) {
			log.error(
					"---PantryDatabase---removeAmountById----THE FINAL AMOUNT IS NEGATIVE-----amountToRevome: {},productAmount: {} ,Final Amount: {}",
					amount, product.get().getAmount(), product.get().getAmount() - amount);
			throw new PantryException(HttpStatus.FORBIDDEN, "The final amount can't be negative");
		}

		var query = defaultDocument(userId).append(Constants.KEY_ID, id);
		log.debug("removeAmountById----QUERY: {}", query.toJson());
		var update = new Document("$inc", new Document(Constants.AMOUNT, amount * -1));
		log.debug("addAmountById----Update: {}", update.toJson());
		return collection.updateOne(query, update).getModifiedCount() > 0;
	}

	@Override
	public Optional<Product> insertProduct(Product product) {
		log.info("--> ---PantryDatabase---insertProduct----. Product: {}", product);
		InsertOneResult result = collection.insertOne(productToDocument(product));
		if (result.getInsertedId() == null) {
			log.error("---PantryDatabase---insertProduct----THE PRODUCT CAN'T BE INSERTED");
			return Optional.empty();
		}
		var query = defaultDocument(product.getUserId()).append(Constants.KEY_ID,
				result.getInsertedId().asString().getValue());
		log.debug("insertProduct--(find to return)--QUERY: {}", query.toJson());
		return Optional.ofNullable(documentToProduct(collection.find(query).first()));
	}

	@Override
	public boolean deleteProduct(String userId, String id) {
		log.info("--> ---PantryDatabase---deleteProduct----. userId: {}, idProduct:{}", userId, id);
		var query = defaultDocument(userId).append(Constants.KEY_ID, id);
		log.debug("deleteProduct-----QUERY: {}", query.toJson());
		DeleteResult result = collection.deleteOne(query);
		log.info("deleteProduct-----WAS DELETED: {}", result.getDeletedCount() > 0);
		return result.getDeletedCount() > 0;

	}

	@Override
	public Optional<Product> updateProduct(String userId, String id, Product product) throws PantryException {
		log.info("--> ---PantryDatabase---updateProduct----. userId: {}, idProduct:{}, product", userId, id, product);

		var query = defaultDocument(userId).append(Constants.KEY_ID, id);
		log.debug("updateProduct-----QUERY: {}", query.toJson());
		var dProduct = productToDocument(product);
		// ignore id for update in db
		dProduct.remove(Constants.KEY_ID);
		var update = new Document("$set", dProduct);
		log.debug("updateProduct-----UPDATE: {}", update.toJson());
		if (collection.updateOne(query, update).getModifiedCount() == 0) {
			log.error("updateProduct-----THE PRODUCT CAN'T BE UPDATED");
			throw new PantryException(HttpStatus.FORBIDDEN, "The product can't be updated");
		}
		// return the product with new values
		return findById(userId, id);
	}

	/**
	 * Find products by query passed as parameter
	 * 
	 * @param doc {@link Document} query to search products
	 * @return {@link List}&lt;{@link Product}&gt;
	 */
	private List<Product> findList(Document doc) {
		log.debug("findList----- QUERY: {}", doc.toJson());
		List<Product> products = new ArrayList<>();
		collection.find(doc).forEach(this.createConsumerForMongoIteratorToList.apply(products));
		return products;
	}

	/**
	 * Return a appropriate Optional depends if the list is empty or not
	 * 
	 * @param products {@link List}&lt;{@link Product}&gt;
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Product}&gt;&gt;. if the
	 *         the list is empty return {@code Optional.empty()} otherwise return a
	 *         {@link Optional} from list passed as parameter
	 */
	private Optional<List<Product>> evaluateList(List<Product> products) {
		return products.isEmpty() ? Optional.empty() : Optional.of(products);
	}

	/**
	 * Create a Default document query with userId
	 * 
	 * @param userId {@link String}
	 * @return {@link Document} with one value appended key:
	 *         <code>{@link USER_ID}</code>, value: {@code userId}
	 */
	private Document defaultDocument(String userId) {
		return new Document().append(Constants.USER_ID, userId);
	}

}
