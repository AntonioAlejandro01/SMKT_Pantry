package com.antonioalejandro.smkt.pantry.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

import com.antonioalejandro.smkt.pantry.model.Category;

/**
 * Category Enum
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 */
public enum CategoryEnum {
    FOOD(1, "FOOD"), KITCHENWARE(2, "KITCHENWARE"), CLEANING(3, "CLEANING"), CLEANING_UTILS(4, "CLEANING_UTILS"),
    OTHERS(5, "OTHERS");

    private final int id;
    private final String value;

    private CategoryEnum(int id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * Return a Category enum by id, if the id doesn't match then return
     * {@link CategoryEnum}.{@code OTHERS}
     * 
     * @param id
     * @return {@link CategoryEnum}
     */
    public static Optional<CategoryEnum> fromId(int id) {
        return Stream.of(CategoryEnum.values()).filter(e -> e.id == id).findFirst();
    }

    /**
     * Convert {@link CategoryEnum} to {@link Category}
     * 
     * @return {@link Category}
     */
    public Category toCategory() {
        Category category = new Category();
        category.setId(id);
        category.setValue(value);
        return category;
    }
}
