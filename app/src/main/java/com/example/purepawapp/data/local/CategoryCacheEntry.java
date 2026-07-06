package com.example.purepawapp.data.local;

import android.provider.BaseColumns;

public final class CategoryCacheEntry implements BaseColumns {
    public static final String TABLE_NAME = "category_cache";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ICON_URL = "icon_url";
    public static final String COLUMN_SORT_ORDER = "sort_order";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ICON_URL + " TEXT, " +
                    COLUMN_SORT_ORDER + " INTEGER NOT NULL DEFAULT 0)";

    private CategoryCacheEntry() {
    }
}
