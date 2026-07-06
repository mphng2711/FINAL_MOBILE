package com.example.purepawapp.data.model

data class Booking(
    val id: String = "",
    val bookingCode: String = "",
    val userId: String = "",
    val pet: Pet = Pet(),
    val serviceId: String = "",
    val serviceName: String = "",
    val serviceType: String = "",
    val bookingDate: String = "",
    val timeSlot: String = "",
    val price: Double = 0.0,
    val status: String = "",
    val cancelReason: String = "",
    val rating: Int = 0,
    val review: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
