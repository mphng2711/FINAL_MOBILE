package com.example.purepawapp.data.model

data class Review(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val productId: String = "",
    val bookingId: String = "",
    val reviewType: String = "",
    val rating: Int = 5,
    val title: String = "",
    val comment: String = "",
    val images: List<String> = emptyList(),
    val isApproved: Boolean = true,
    val adminReply: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
