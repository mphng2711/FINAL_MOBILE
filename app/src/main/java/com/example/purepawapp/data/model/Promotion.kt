package com.example.purepawapp.data.model

data class Promotion(
    val code: String = "",
    val description: String = "",
    val discountType: String = "",
    val discountValue: Double = 0.0,
    val minOrderAmount: Double = 0.0,
    val maxDiscountAmount: Double = 0.0,
    val usageLimit: Int = 0,
    val usedCount: Int = 0,
    val usageLimitPerUser: Int = 1,
    val applicableTo: String = "all",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val isActive: Boolean = true
)
