package com.example.purepawapp.data.model

data class ProductVariant(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val salePrice: Double? = null,
    val sku: String = "",
    val stock: Int = 0
) {
    val effectivePrice: Double get() = salePrice ?: price
}
