package com.example.purepawapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductCacheDao {

    private final PurePawDbHelper dbHelper;

    public ProductCacheDao(Context context) {
        this.dbHelper = new PurePawDbHelper(context.getApplicationContext());
    }

    public void cacheProducts(List<Product> products) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(ProductCacheEntry.TABLE_NAME, null, null);
            for (Product product : products) {
                String description = product.getShortDescription();
                if (description == null || description.isBlank()) {
                    description = product.getDescription();
                }
                ContentValues values = new ContentValues();
                values.put(ProductCacheEntry.COLUMN_ID, product.getId());
                values.put(ProductCacheEntry.COLUMN_NAME, product.getName());
                values.put(ProductCacheEntry.COLUMN_CATEGORY_ID, product.getCategoryId());
                values.put(ProductCacheEntry.COLUMN_PRICE, product.getDisplayPrice());
                values.put(ProductCacheEntry.COLUMN_IMAGE_URL, product.getDisplayImage());
                values.put(ProductCacheEntry.COLUMN_DESCRIPTION, description);
                values.put(ProductCacheEntry.COLUMN_RATING, product.getAverageRating());
                values.put(ProductCacheEntry.COLUMN_REVIEW_COUNT, product.getTotalReviews());
                values.put(ProductCacheEntry.COLUMN_TOTAL_SOLD, product.getTotalSold());
                values.put(ProductCacheEntry.COLUMN_CACHED_AT, System.currentTimeMillis());
                db.insertWithOnConflict(ProductCacheEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Product> getCachedProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = new ArrayList<>();
        try (Cursor cursor = db.query(ProductCacheEntry.TABLE_NAME, null, null, null, null, null,
                ProductCacheEntry.COLUMN_TOTAL_SOLD + " DESC")) {
            while (cursor.moveToNext()) {
                double cachedPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_PRICE));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_IMAGE_URL));
                if (imageUrl == null) imageUrl = "";

                Product product = new Product();
                product.setId(cursor.getString(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_NAME)));
                product.setCategoryId(cursor.getString(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_CATEGORY_ID)));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_DESCRIPTION));
                product.setDescription(description == null ? "" : description);
                product.setImages(imageUrl.isEmpty() ? Collections.emptyList() : Collections.singletonList(imageUrl));
                product.setThumbnail(imageUrl);

                ProductVariant variant = new ProductVariant();
                variant.setId("cached");
                variant.setName("Default");
                variant.setPrice(cachedPrice);
                product.setVariants(Collections.singletonList(variant));

                product.setAverageRating(cursor.getDouble(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_RATING)));
                product.setTotalReviews(cursor.getInt(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_REVIEW_COUNT)));
                product.setTotalSold(cursor.getInt(cursor.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_TOTAL_SOLD)));
                products.add(product);
            }
        }
        return products;
    }

    public void cacheCategories(List<Category> categories) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(CategoryCacheEntry.TABLE_NAME, null, null);
            for (Category category : categories) {
                ContentValues values = new ContentValues();
                values.put(CategoryCacheEntry.COLUMN_ID, category.getId());
                values.put(CategoryCacheEntry.COLUMN_NAME, category.getName());
                values.put(CategoryCacheEntry.COLUMN_ICON_URL, category.getImage());
                values.put(CategoryCacheEntry.COLUMN_SORT_ORDER, category.getSortOrder());
                db.insertWithOnConflict(CategoryCacheEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Category> getCachedCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Category> categories = new ArrayList<>();
        try (Cursor cursor = db.query(CategoryCacheEntry.TABLE_NAME, null, null, null, null, null,
                CategoryCacheEntry.COLUMN_SORT_ORDER + " ASC")) {
            while (cursor.moveToNext()) {
                Category category = new Category();
                category.setId(cursor.getString(cursor.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_ID)));
                category.setName(cursor.getString(cursor.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_NAME)));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_ICON_URL));
                category.setImage(image == null ? "" : image);
                category.setSortOrder(cursor.getInt(cursor.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_SORT_ORDER)));
                categories.add(category);
            }
        }
        return categories;
    }
}
