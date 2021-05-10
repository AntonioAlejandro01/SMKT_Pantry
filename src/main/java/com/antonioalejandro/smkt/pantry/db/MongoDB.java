package com.antonioalejandro.smkt.pantry.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.utils.Mappers;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.types.ObjectId;

import lombok.extern.slf4j.Slf4j;

/**
 * Mongo DB Class TODO: Methods not implemented
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @see Mappers
 * @apiNote Class that manage product database
 */
@Slf4j
public class MongoDB implements PantryDatabase, Mappers {

    private MongoCollection<Document> collection;
    private Function<List<Product>, Consumer<Document>> createConsumerForMongoIteratorToList;

    /**
     * Create a MongoDB instance
     * 
     * @param connectionString connection string
     * @param database         database name
     * @param schema           schema name
     */
    public MongoDB(String connectionString, String database, String schema) {
        log.info("ConnectionString {}", connectionString);
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase db = client.getDatabase(database);
        collection = db.getCollection(schema);

        // Functions
        this.createConsumerForMongoIteratorToList = products -> doc -> products.add(this.documentToProduct(doc));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Product>> findAll(String userId) {
        return evaluateList(findList(defaultDocument(userId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findById(String id, String userId) {

        return Optional.ofNullable(collection.find(defaultDocument(userId).append(KEY_ID, id)).first())
                .map(this::documentToProduct);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Product>> findByName(String name, String userId) {
        Document query = defaultDocument(userId).append(NAME, new Document("$regex", name));
        return evaluateList(findList(query));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findByCodeKey(String codeKey, String userId) {
        return Optional.ofNullable(collection.find(defaultDocument(userId).append(CODE_KEY, codeKey)).first())
                .map(this::documentToProduct);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Product>> findByPrice(String userId, double price) {
        Document query = defaultDocument(userId).append(PRICE, new Document().append("$lte", price));
        return evaluateList(findList(query));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Product>> findByCategory(String userId, int categoryId) {
        Document query = defaultDocument(userId).append(String.format("%s.%s", CATEGORY, KEY_ID), categoryId);
        return evaluateList(findList(query));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Product>> findByAmount(String userId, int amount) {
        Document query = defaultDocument(userId).append(AMOUNT, new Document("$lte", amount));
        return evaluateList(findList(query));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAmountById(String userId, String id, int amount) {
        Document query = defaultDocument(userId).append(KEY_ID, id);
        Document dataToUpdate = new Document("$inc", new Document(AMOUNT, amount));
        return collection.updateOne(query, dataToUpdate).wasAcknowledged();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAmountById(String userId, String id, int amount) {
        Optional<Product> product = findById(id, userId);
        if (product.isEmpty()) {
            return false;
            // TODO: throw Error
        }
        if (product.get().getAmount() - amount < 0) {
            return false;
            // TODO: Throw ErrorService
        }

        Document query = defaultDocument(userId).append(KEY_ID, id);
        Document update = new Document("$inc", new Document(AMOUNT, amount * -1));
        return collection.updateOne(query, update).wasAcknowledged();
    }

    @Override
    public Optional<Product> insertProduct(Product product) {
        InsertOneResult result = collection.insertOne(productToDocument(product));
        if (!result.wasAcknowledged()) {
            return Optional.empty();
        }
        Document query = defaultDocument(product.getUserId()).append(KEY_ID,
                result.getInsertedId().asString().toString());
        return Optional.ofNullable(documentToProduct(collection.find(query).first()));
    }

    @Override
    public boolean deleteProduct(String userId, String id) {
        Document query = defaultDocument(userId).append(KEY_ID, new ObjectId(id));
        DeleteResult result = collection.deleteOne(query);
        return result.wasAcknowledged();

    }

    /**
     * Find products by query passed as parameter
     * 
     * @param doc {@link Document} query to search products
     * @return {@link List}&lt;{@link Product}&gt;
     */
    private List<Product> findList(Document doc) {
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
        return new Document().append(USER_ID, userId);
    }

}
