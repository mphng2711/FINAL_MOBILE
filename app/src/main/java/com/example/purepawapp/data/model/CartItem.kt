package com.example.purepawapp.data.model

data class CartItem(
    val product: Product,
    val variant: ProductVariant,
    val quantity: Int = 1
) {
    val unitPrice: Double get() = variant.effectivePrice
    val lineTotal: Double get() = unitPrice * quantity
}
