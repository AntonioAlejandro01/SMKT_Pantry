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
public interface Mappers {

    /**
     * Map class {@link Product} to {@link Document} The id is exclude
     * 
     * @param product {@link Product}
     * @return {@link Document}
     */
    default Document productToDocument(Product product) {
        return new Document().append(Constants.KEY_ID, product.getId()).append(Constants.USER_ID, product.getUserId())
                .append(Constants.CODE_KEY, product.getCodeKey()).append(Constants.CATEGORY, categoryToDocument(product.getCategory()))
                .append(Constants.AMOUNT, product.getAmount()).append(Constants.PRICE, product.getPrice()).append(Constants.NAME, product.getName());

    }

    /**
     * Map class {@link Category} to {@link Document}
     * 
     * @param category {@link Category}
     * @return {@link Document}
     */
    default Document categoryToDocument(Category category) {
        return new Document().append(Constants.KEY_ID, category.getId()).append(Constants.VALUE, category.getValue());
    }

    /**
     * Map class {@link Document} to {@link Product}
     * 
     * @param doc {@link Document}
     * @return {@link Product}
     */
    default Product documentToProduct(Document doc) {
        var product = new Product();
        product.setAmount(doc.getInteger(Constants.AMOUNT));
        product.setId(doc.get(Constants.KEY_ID).toString());
        product.setCodeKey(doc.getString(Constants.CODE_KEY));
        product.setName(doc.getString(Constants.NAME));
        product.setPrice(doc.getDouble(Constants.PRICE));
        product.setUserId(doc.getString(Constants.USER_ID));
        product.setCategory(documentToCategory((Document) doc.get(Constants.CATEGORY)));
        return product;

    }

    /**
     * Map class {@link Document} to {@link Category}
     * 
     * @param doc {@link Document}
     * @return {@link Category}
     */
    default Category documentToCategory(Document doc) {
        var category = new Category();
        category.setId(doc.getInteger(Constants.KEY_ID));
        category.setValue(doc.getString(Constants.VALUE));
        return category;
    }

}
