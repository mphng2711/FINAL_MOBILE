package com.example.purepawapp.ui.product;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
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

    public static void loadImage(ImageView imageView, Product product) {
        loadImage(imageView, product.getDisplayImage());
    }

    public static void loadImage(ImageView imageView, String url) {
        if (url == null || url.isBlank()) {
            imageView.setImageDrawable(null);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .into(imageView);
    }
}
