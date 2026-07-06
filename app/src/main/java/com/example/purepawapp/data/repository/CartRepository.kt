package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.CartItem
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.data.model.ProductVariant
import com.example.purepawapp.data.model.Promotion
import com.example.purepawapp.util.DiscountType
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

interface CartRepository {
    val items: StateFlow<List<CartItem>>
    val appliedPromotion: StateFlow<Promotion?>
    fun addToCart(product: Product, variant: ProductVariant, quantity: Int = 1)
    fun updateQuantity(productId: String, variantId: String, quantity: Int)
    fun removeFromCart(productId: String, variantId: String)
    fun clearCart()
    suspend fun applyPromoCode(code: String): Result<Promotion>
    fun clearPromotion()
    fun subtotal(): Double
    fun discount(): Double
    fun total(): Double
}

class CartRepositoryImpl(
    private val firestore: FirebaseFirestore
) : CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val _appliedPromotion = MutableStateFlow<Promotion?>(null)
    override val appliedPromotion: StateFlow<Promotion?> = _appliedPromotion.asStateFlow()

    override fun addToCart(product: Product, variant: ProductVariant, quantity: Int) {
        val current = _items.value.toMutableList()
        val existingIndex = current.indexOfFirst {
            it.product.id == product.id && it.variant.id == variant.id
        }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current += CartItem(product = product, variant = variant, quantity = quantity)
        }
        _items.value = current
    }

    override fun updateQuantity(productId: String, variantId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId, variantId)
            return
        }
        _items.value = _items.value.map {
            if (it.product.id == productId && it.variant.id == variantId) it.copy(quantity = quantity) else it
        }
    }

    override fun removeFromCart(productId: String, variantId: String) {
        _items.value = _items.value.filterNot { it.product.id == productId && it.variant.id == variantId }
    }

    override fun clearCart() {
        _items.value = emptyList()
        _appliedPromotion.value = null
    }

    override suspend fun applyPromoCode(code: String): Result<Promotion> = runCatching {
        val doc = firestore.collection(FirestoreCollections.PROMOTIONS).document(code.uppercase()).get().await()
        val promotion = doc.toObject(Promotion::class.java) ?: error("Invalid promo code")
        val now = System.currentTimeMillis()
        if (!promotion.isActive) error("Promo code is no longer active")
        if (promotion.endDate in 1..<now) error("Promo code expired")
        if (promotion.usageLimit in 1..promotion.usedCount) error("Promo code usage limit reached")
        if (subtotal() < promotion.minOrderAmount) error("Order does not meet minimum value for this code")
        _appliedPromotion.value = promotion
        promotion
    }

    override fun clearPromotion() {
        _appliedPromotion.value = null
    }

    override fun subtotal(): Double = _items.value.sumOf { it.lineTotal }

    override fun discount(): Double {
        val promo = _appliedPromotion.value ?: return 0.0
        val subtotal = subtotal()
        val rawDiscount = if (promo.discountType == DiscountType.PERCENTAGE) {
            subtotal * (promo.discountValue / 100.0)
        } else {
            promo.discountValue
        }
        return if (promo.maxDiscountAmount > 0) rawDiscount.coerceAtMost(promo.maxDiscountAmount) else rawDiscount
    }

    override fun total(): Double = (subtotal() - discount()).coerceAtLeast(0.0)
}
