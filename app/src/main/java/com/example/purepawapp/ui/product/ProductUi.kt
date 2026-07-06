package com.example.purepawapp.ui.product

import com.example.purepawapp.data.model.Product

private val categoryEmojis = mapOf(
    "food" to "🦴",
    "toys" to "🎾",
    "accessories" to "🎀",
    "clothing" to "👕",
    "health" to "💊"
)

val Product.emoji: String get() = categoryEmojis[categoryId] ?: "🐾"

val Product.badgeLabel: String get() = if (totalSold > 0) "Bán chạy" else "Mới"
