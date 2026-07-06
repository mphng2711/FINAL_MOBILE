package com.example.purepawapp.data.local;

import android.provider.BaseColumns;

public final class ProductCacheEntry implements BaseColumns {
    public static final String TABLE_NAME = "product_cache";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_REVIEW_COUNT = "review_count";
    public static final String COLUMN_TOTAL_SOLD = "total_sold";
    public static final String COLUMN_CACHED_AT = "cached_at";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_CATEGORY_ID + " TEXT NOT NULL, " +
                    COLUMN_PRICE + " REAL NOT NULL, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_RATING + " REAL NOT NULL DEFAULT 0, " +
                    COLUMN_REVIEW_COUNT + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_TOTAL_SOLD + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_CACHED_AT + " INTEGER NOT NULL)";

    private ProductCacheEntry() {
    }
}
