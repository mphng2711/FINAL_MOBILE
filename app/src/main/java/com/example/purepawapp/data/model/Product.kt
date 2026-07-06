package com.example.purepawapp.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val slug: String = "",
    val categoryId: String = "",
    val description: String = "",
    val shortDescription: String = "",
    val images: List<String> = emptyList(),
    val thumbnail: String = "",
    val variants: List<ProductVariant> = emptyList(),
    val brand: String = "",
    val petType: String = "",
    val tags: List<String> = emptyList(),
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val totalSold: Int = 0,
    val isActive: Boolean = true,
    val isFeatured: Boolean = false
) {
    val defaultVariant: ProductVariant? get() = variants.firstOrNull()
    val displayPrice: Double get() = defaultVariant?.effectivePrice ?: 0.0
    val displayImage: String get() = thumbnail.ifBlank { images.firstOrNull().orEmpty() }
}
