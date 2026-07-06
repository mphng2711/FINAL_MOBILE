package com.example.purepawapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.purepawapp.data.model.CartItem;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;
import com.example.purepawapp.data.model.Promotion;
import com.example.purepawapp.util.DiscountType;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartRepositoryImpl implements CartRepository {

    private final FirebaseFirestore firestore;
    private final MutableLiveData<List<CartItem>> items = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Promotion> appliedPromotion = new MutableLiveData<>(null);

    public CartRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public LiveData<List<CartItem>> getItemsLiveData() {
        return items;
    }

    @Override
    public List<CartItem> getItems() {
        List<CartItem> current = items.getValue();
        return current == null ? Collections.emptyList() : current;
    }

    @Override
    public LiveData<Promotion> getAppliedPromotionLiveData() {
        return appliedPromotion;
    }

    @Override
    public Promotion getAppliedPromotion() {
        return appliedPromotion.getValue();
    }

    @Override
    public void addToCart(Product product, ProductVariant variant, int quantity) {
        List<CartItem> current = new ArrayList<>(getItems());
        int existingIndex = -1;
        for (int i = 0; i < current.size(); i++) {
            CartItem item = current.get(i);
            if (item.getProduct().getId().equals(product.getId()) && item.getVariant().getId().equals(variant.getId())) {
                existingIndex = i;
                break;
            }
        }
        if (existingIndex >= 0) {
            CartItem existing = current.get(existingIndex);
            current.set(existingIndex, new CartItem(existing.getProduct(), existing.getVariant(), existing.getQuantity() + quantity));
        } else {
            current.add(new CartItem(product, variant, quantity));
        }
        items.setValue(current);
    }

    @Override
    public void updateQuantity(String productId, String variantId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(productId, variantId);
            return;
        }
        List<CartItem> updated = new ArrayList<>();
        for (CartItem item : getItems()) {
            if (item.getProduct().getId().equals(productId) && item.getVariant().getId().equals(variantId)) {
                updated.add(new CartItem(item.getProduct(), item.getVariant(), quantity));
            } else {
                updated.add(item);
            }
        }
        items.setValue(updated);
    }

    @Override
    public void removeFromCart(String productId, String variantId) {
        List<CartItem> updated = new ArrayList<>();
        for (CartItem item : getItems()) {
            if (!(item.getProduct().getId().equals(productId) && item.getVariant().getId().equals(variantId))) {
                updated.add(item);
            }
        }
        items.setValue(updated);
    }

    @Override
    public void clearCart() {
        items.setValue(new ArrayList<>());
        appliedPromotion.setValue(null);
    }

    @Override
    public void applyPromoCode(String code, RepoCallback<Promotion> callback) {
        firestore.collection(FirestoreCollections.PROMOTIONS).document(code.toUpperCase()).get()
                .addOnSuccessListener(doc -> {
                    Promotion promotion = doc.toObject(Promotion.class);
                    if (promotion == null) {
                        callback.onError(new IllegalStateException("Invalid promo code"));
                        return;
                    }
                    long now = System.currentTimeMillis();
                    if (!promotion.isActive()) {
                        callback.onError(new IllegalStateException("Promo code is no longer active"));
                        return;
                    }
                    if (promotion.getEndDate() >= 1 && promotion.getEndDate() < now) {
                        callback.onError(new IllegalStateException("Promo code expired"));
                        return;
                    }
                    if (promotion.getUsageLimit() >= 1 && promotion.getUsedCount() >= promotion.getUsageLimit()) {
                        callback.onError(new IllegalStateException("Promo code usage limit reached"));
                        return;
                    }
                    if (subtotal() < promotion.getMinOrderAmount()) {
                        callback.onError(new IllegalStateException("Order does not meet minimum value for this code"));
                        return;
                    }
                    appliedPromotion.setValue(promotion);
                    callback.onSuccess(promotion);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void clearPromotion() {
        appliedPromotion.setValue(null);
    }

    @Override
    public double subtotal() {
        double sum = 0;
        for (CartItem item : getItems()) {
            sum += item.getLineTotal();
        }
        return sum;
    }

    @Override
    public double discount() {
        Promotion promo = getAppliedPromotion();
        if (promo == null) return 0.0;
        double subtotal = subtotal();
        double rawDiscount = DiscountType.PERCENTAGE.equals(promo.getDiscountType())
                ? subtotal * (promo.getDiscountValue() / 100.0)
                : promo.getDiscountValue();
        return promo.getMaxDiscountAmount() > 0 ? Math.min(rawDiscount, promo.getMaxDiscountAmount()) : rawDiscount;
    }

    @Override
    public double total() {
        return Math.max(subtotal() - discount(), 0.0);
    }
}
