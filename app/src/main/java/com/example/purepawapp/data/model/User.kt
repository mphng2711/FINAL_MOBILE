package com.example.purepawapp.data.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val avatarUrl: String = "",
    val address: Address = Address(),
    val role: String = "user",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
