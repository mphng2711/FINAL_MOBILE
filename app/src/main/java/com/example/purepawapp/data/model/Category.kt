package com.example.purepawapp.data.model

data class Category(
    val id: String = "",
    val name: String = "",
    val slug: String = "",
    val description: String = "",
    val image: String = "",
    val parentCategoryId: String? = null,
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)
