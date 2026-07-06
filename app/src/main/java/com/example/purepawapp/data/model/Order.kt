package com.example.purepawapp.data.model

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val variantId: String = "",
    val variantName: String = "",
    val imageUrl: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0,
    val subtotal: Double = 0.0
)

data class OrderStatusEvent(
    val status: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Order(
    val id: String = "",
    val orderCode: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val shippingAddress: Address = Address(),
    val couponCode: String = "",
    val subtotal: Double = 0.0,
    val discountAmount: Double = 0.0,
    val shippingFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "",
    val status: String = "",
    val statusHistory: List<OrderStatusEvent> = emptyList(),
    val note: String = "",
    val cancelReason: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
