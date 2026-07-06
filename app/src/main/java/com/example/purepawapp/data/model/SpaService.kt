package com.example.purepawapp.data.model

data class SpaService(
    val id: String = "",
    val name: String = "",
    val serviceType: String = "",
    val description: String = "",
    val durationMinutes: Int = 0,
    val price: Double = 0.0,
    val iconUrl: String = ""
)
