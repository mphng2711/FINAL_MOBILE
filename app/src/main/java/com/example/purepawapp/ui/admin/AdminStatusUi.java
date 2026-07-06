package com.example.purepawapp.ui.admin;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.util.BookingStatus;
import com.example.purepawapp.util.OrderStatus;

import java.util.List;
import java.util.Map;

public final class AdminStatusUi {

    private static final Map<String, String> DEFAULT_CATEGORY_EMOJIS = Map.of(
            "food", "🦴",
            "toys", "🎾",
            "accessories", "🎀",
            "clothing", "👕",
            "health", "💊"
    );

    private static final Map<String, String> DEFAULT_CATEGORY_NAMES = Map.of(
            "food", "Thức ăn",
            "toys", "Đồ chơi",
            "accessories", "Phụ kiện",
            "clothing", "Quần áo",
            "health", "Chăm sóc sức khỏe"
    );

    public static final List<String> ORDER_STATUS_FLOW = List.of(
            OrderStatus.PENDING, OrderStatus.CONFIRMED, OrderStatus.PROCESSING,
            OrderStatus.SHIPPING, OrderStatus.DELIVERED, OrderStatus.CANCELLED
    );

    public static final List<String> BOOKING_STATUS_FLOW = List.of(
            BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.IN_PROGRESS,
            BookingStatus.COMPLETED, BookingStatus.CANCELLED
    );

    private AdminStatusUi() {
    }

    public static String categoryEmoji(Category category) {
        if (category.getImage() != null && !category.getImage().isBlank()) return category.getImage();
        return DEFAULT_CATEGORY_EMOJIS.getOrDefault(category.getId(), "🗂️");
    }

    public static String categoryDisplayName(String categoryId) {
        return DEFAULT_CATEGORY_NAMES.getOrDefault(categoryId, categoryId);
    }

    public static String toCompactVnd(double value) {
        if (value >= 1_000_000_000) return String.format("%.1fB", value / 1_000_000_000);
        if (value >= 1_000_000) return String.format("%.1fM", value / 1_000_000);
        if (value >= 1_000) return String.format("%.0fK", value / 1_000);
        return String.valueOf((int) value);
    }

    public static StatusStyle orderStatusStyle(String status) {
        if (OrderStatus.PENDING.equals(status)) return new StatusStyle("Chờ xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text);
        if (OrderStatus.CONFIRMED.equals(status)) return new StatusStyle("Đã xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text);
        if (OrderStatus.PROCESSING.equals(status)) return new StatusStyle("Đang xử lý", R.color.pp_chip_orange_bg, R.color.pp_primary_dark);
        if (OrderStatus.SHIPPING.equals(status)) return new StatusStyle("Đang giao", R.color.pp_chip_orange_bg, R.color.pp_primary_dark);
        if (OrderStatus.DELIVERED.equals(status)) return new StatusStyle("Đã giao", R.color.pp_chip_green_bg, R.color.pp_success_dark);
        if (OrderStatus.CANCELLED.equals(status)) return new StatusStyle("Đã hủy", R.color.pp_status_red_bg, R.color.pp_status_red_text);
        if (OrderStatus.RETURNED.equals(status)) return new StatusStyle("Đã hoàn trả", R.color.pp_chip_gray_bg, R.color.pp_text_secondary);
        return new StatusStyle(status, R.color.pp_chip_gray_bg, R.color.pp_text_secondary);
    }

    public static StatusStyle bookingStatusStyle(String status) {
        if (BookingStatus.PENDING.equals(status)) return new StatusStyle("Chờ xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text);
        if (BookingStatus.CONFIRMED.equals(status)) return new StatusStyle("Đã xác nhận", R.color.pp_status_blue_bg, R.color.pp_status_blue_text);
        if (BookingStatus.IN_PROGRESS.equals(status)) return new StatusStyle("Đang thực hiện", R.color.pp_chip_orange_bg, R.color.pp_primary_dark);
        if (BookingStatus.COMPLETED.equals(status)) return new StatusStyle("Hoàn thành", R.color.pp_chip_green_bg, R.color.pp_success_dark);
        if (BookingStatus.CANCELLED.equals(status)) return new StatusStyle("Đã hủy", R.color.pp_status_red_bg, R.color.pp_status_red_text);
        return new StatusStyle(status, R.color.pp_chip_gray_bg, R.color.pp_text_secondary);
    }
}
