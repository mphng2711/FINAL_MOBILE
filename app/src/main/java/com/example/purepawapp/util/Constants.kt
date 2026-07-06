package com.example.purepawapp.util

object FirestoreCollections {
    const val USERS = "users"
    const val CATEGORIES = "categories"
    const val PRODUCTS = "products"
    const val REVIEWS = "reviews"
    const val BLOGS = "blogs"
    const val SPA_SERVICES = "spaServices"
    const val ORDERS = "orders"
    const val BOOKINGS = "bookings"
    const val PROMOTIONS = "promotions"
    const val BOOKED_SLOTS = "bookedSlots"
}

object PrefsKeys {
    const val DATASTORE_NAME = "purepaw_prefs"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_USER_ID = "user_id"
    const val KEY_ROLE = "role"
    const val KEY_ONBOARDING_SEEN = "onboarding_seen"
    const val KEY_REMEMBER_ME = "remember_me"
    const val KEY_BOOTSTRAP_DONE = "bootstrap_done"
}

object SqliteConfig {
    const val DATABASE_NAME = "purepaw_cache.db"
    const val DATABASE_VERSION = 2
}

object OrderStatus {
    const val PENDING = "pending"
    const val CONFIRMED = "confirmed"
    const val PROCESSING = "processing"
    const val SHIPPING = "shipping"
    const val DELIVERED = "delivered"
    const val CANCELLED = "cancelled"
    const val RETURNED = "returned"
}

object BookingStatus {
    const val PENDING = "pending"
    const val CONFIRMED = "confirmed"
    const val IN_PROGRESS = "in_progress"
    const val COMPLETED = "completed"
    const val CANCELLED = "cancelled"
}

object DiscountType {
    const val PERCENTAGE = "percentage"
    const val FIXED_AMOUNT = "fixed_amount"
}
