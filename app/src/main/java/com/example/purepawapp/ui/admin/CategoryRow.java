package com.example.purepawapp.ui.admin;

import com.example.purepawapp.data.model.Category;

public class CategoryRow {
    private final Category category;
    private final int productCount;

    public CategoryRow(Category category, int productCount) {
        this.category = category;
        this.productCount = productCount;
    }

    public Category getCategory() {
        return category;
    }

    public int getProductCount() {
        return productCount;
    }
}
