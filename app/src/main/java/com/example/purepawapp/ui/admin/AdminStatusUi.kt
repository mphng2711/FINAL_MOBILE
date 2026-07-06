package com.example.purepawapp.ui.admin

import com.example.purepawapp.R
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.util.BookingStatus
import com.example.purepawapp.util.OrderStatus

private val defaultCategoryEmojis = mapOf(
    "food" to "🦴",
    "toys" to "🎾",
    "accessories" to "🎀",
    "clothing" to "👕",
    "health" to "💊"
)

fun categoryEmoji(category: Category): String =
    category.image.ifBlank { defaultCategoryEmojis[category.id] ?: "🗂️" }

private val defaultCategoryNames = mapOf(
    "food" to "Thức ăn",
    "toys" to "Đồ chơi",
    "accessories" to "Phụ kiện",
    "clothing" to "Quần áo",
    "health" to "Chăm sóc sức khỏe"
)

fun categoryDisplayName(categoryId: String): String = defaultCategoryNames[categoryId] ?: categoryId

fun Double.toCompactVnd(): String = when {
    this >= 1_000_000_000 -> "%.1fB".format(this / 1_000_000_000)
    this >= 1_000_000 -> "%.1fM".format(this / 1_000_000)
    this >= 1_000 -> "%.0fK".format(this / 1_000)
    else -> this.toInt().toString()
}

data class StatusStyle(val label: String, val bgColorRes: Int, val textColorRes: Int)

fun orderStatusStyle(status: String): StatusStyle = when (status) {
    OrderStatus.PENDING -> StatusStyle("Chờ xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text)
    OrderStatus.CONFIRMED -> StatusStyle("Đã xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text)
    OrderStatus.PROCESSING -> StatusStyle("Đang xử lý", R.color.pp_chip_orange_bg, R.color.pp_primary_dark)
    OrderStatus.SHIPPING -> StatusStyle("Đang giao", R.color.pp_chip_orange_bg, R.color.pp_primary_dark)
    OrderStatus.DELIVERED -> StatusStyle("Đã giao", R.color.pp_chip_green_bg, R.color.pp_success_dark)
    OrderStatus.CANCELLED -> StatusStyle("Đã hủy", R.color.pp_status_red_bg, R.color.pp_status_red_text)
    OrderStatus.RETURNED -> StatusStyle("Đã hoàn trả", R.color.pp_chip_gray_bg, R.color.pp_text_secondary)
    else -> StatusStyle(status, R.color.pp_chip_gray_bg, R.color.pp_text_secondary)
}

fun bookingStatusStyle(status: String): StatusStyle = when (status) {
    BookingStatus.PENDING -> StatusStyle("Chờ xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text)
    BookingStatus.CONFIRMED -> StatusStyle("Đã xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text)
    BookingStatus.IN_PROGRESS -> StatusStyle("Đang thực hiện", R.color.pp_chip_orange_bg, R.color.pp_primary_dark)
    BookingStatus.COMPLETED -> StatusStyle("Hoàn thành", R.color.pp_chip_green_bg, R.color.pp_success_dark)
    BookingStatus.CANCELLED -> StatusStyle("Đã hủy", R.color.pp_status_red_bg, R.color.pp_status_red_text)
    else -> StatusStyle(status, R.color.pp_chip_gray_bg, R.color.pp_text_secondary)
}

val orderStatusFlow = listOf(
    OrderStatus.PENDING,
    OrderStatus.CONFIRMED,
    OrderStatus.PROCESSING,
    OrderStatus.SHIPPING,
    OrderStatus.DELIVERED,
    OrderStatus.CANCELLED
)

val bookingStatusFlow = listOf(
    BookingStatus.PENDING,
    BookingStatus.CONFIRMED,
    BookingStatus.IN_PROGRESS,
    BookingStatus.COMPLETED,
    BookingStatus.CANCELLED
)
