package com.example.purepawapp.ui.product;

import com.example.purepawapp.data.model.Product;

import java.util.Map;

public final class ProductUi {

    private static final Map<String, String> CATEGORY_EMOJIS = Map.of(
            "food", "🦴",
            "toys", "🎾",
            "accessories", "🎀",
            "clothing", "👕",
            "health", "💊"
    );

    private ProductUi() {
    }

    public static String getEmoji(Product product) {
        return CATEGORY_EMOJIS.getOrDefault(product.getCategoryId(), "🐾");
    }

    public static String getBadgeLabel(Product product) {
        return product.getTotalSold() > 0 ? "Bán chạy" : "Mới";
    }
}
