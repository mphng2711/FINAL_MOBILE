package com.example.purepawapp.data.local

import android.provider.BaseColumns

object ProductCacheEntry : BaseColumns {
    const val TABLE_NAME = "product_cache"
    const val COLUMN_ID = "_id"
    const val COLUMN_NAME = "name"
    const val COLUMN_CATEGORY_ID = "category_id"
    const val COLUMN_PRICE = "price"
    const val COLUMN_IMAGE_URL = "image_url"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_RATING = "rating"
    const val COLUMN_REVIEW_COUNT = "review_count"
    const val COLUMN_TOTAL_SOLD = "total_sold"
    const val COLUMN_CACHED_AT = "cached_at"

    const val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID TEXT PRIMARY KEY,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_CATEGORY_ID TEXT NOT NULL,
            $COLUMN_PRICE REAL NOT NULL,
            $COLUMN_IMAGE_URL TEXT,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_RATING REAL NOT NULL DEFAULT 0,
            $COLUMN_REVIEW_COUNT INTEGER NOT NULL DEFAULT 0,
            $COLUMN_TOTAL_SOLD INTEGER NOT NULL DEFAULT 0,
            $COLUMN_CACHED_AT INTEGER NOT NULL
        )
    """
}

object CategoryCacheEntry : BaseColumns {
    const val TABLE_NAME = "category_cache"
    const val COLUMN_ID = "_id"
    const val COLUMN_NAME = "name"
    const val COLUMN_ICON_URL = "icon_url"
    const val COLUMN_SORT_ORDER = "sort_order"

    const val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID TEXT PRIMARY KEY,
            $COLUMN_NAME TEXT NOT NULL,
            $COLUMN_ICON_URL TEXT,
            $COLUMN_SORT_ORDER INTEGER NOT NULL DEFAULT 0
        )
    """
}
