package com.example.purepawapp.data.repository;

import androidx.lifecycle.LiveData;

import com.example.purepawapp.data.model.CartItem;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;
import com.example.purepawapp.data.model.Promotion;

import java.util.List;

public interface CartRepository {
    LiveData<List<CartItem>> getItemsLiveData();

    List<CartItem> getItems();

    LiveData<Promotion> getAppliedPromotionLiveData();

    Promotion getAppliedPromotion();

    void addToCart(Product product, ProductVariant variant, int quantity);

    void updateQuantity(String productId, String variantId, int quantity);

    void removeFromCart(String productId, String variantId);

    void clearCart();

    void applyPromoCode(String code, RepoCallback<Promotion> callback);

    void clearPromotion();

    double subtotal();

    double discount();

    double total();
}
