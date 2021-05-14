package com.antonioalejandro.smkt.pantry.utils;

import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.Product;

import org.bson.Document;

/**
 * Mappers interface
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.comç
 * @see ConstantsMappers
 * @version 1.0.0
 * @apiNote Interface for map between {@link Document} , {@link Product} and
 *          {@link Category}
 */
public interface Mappers extends ConstantsMappers {

    /**
     * Map class {@link Product} to {@link Document} The id is exclude
     * 
     * @param product {@link Product}
     * @return {@link Document}
     */
    default Document productToDocument(Product product) {
        return new Document().append(KEY_ID, product.getId()).append(USER_ID, product.getUserId())
                .append(CODE_KEY, product.getCodeKey()).append(CATEGORY, categoryToDocument(product.getCategory()))
                .append(AMOUNT, product.getAmount()).append(PRICE, product.getPrice()).append(NAME, product.getName());

    }

    /**
     * Map class {@link Category} to {@link Document}
     * 
     * @param category {@link Category}
     * @return {@link Document}
     */
    default Document categoryToDocument(Category category) {
        return new Document().append(KEY_ID, category.getId()).append(VALUE, category.getValue());
    }

    /**
     * Map class {@link Document} to {@link Product}
     * 
     * @param doc {@link Document}
     * @return {@link Product}
     */
    default Product documentToProduct(Document doc) {
        Product product = new Product();
        product.setAmount(doc.getInteger(AMOUNT));
        product.setId(doc.get(KEY_ID).toString());
        product.setCodeKey(doc.getString(CODE_KEY));
        product.setName(doc.getString(NAME));
        product.setPrice(doc.getDouble(PRICE));
        product.setUserId(doc.getString(USER_ID));
        product.setCategory(documentToCategory((Document) doc.get(CATEGORY)));
        return product;

    }

    /**
     * Map class {@link Document} to {@link Category}
     * 
     * @param doc {@link Document}
     * @return {@link Category}
     */
    default Category documentToCategory(Document doc) {
        Category category = new Category();
        category.setId(doc.getInteger(KEY_ID));
        category.setValue(doc.getString(VALUE));
        return category;
    }

}
