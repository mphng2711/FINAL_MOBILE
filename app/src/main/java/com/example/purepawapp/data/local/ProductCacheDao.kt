package com.example.purepawapp.data.local

import android.content.ContentValues
import android.content.Context
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.data.model.ProductVariant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductCacheDao(context: Context) {

    private val dbHelper = PurePawDbHelper(context.applicationContext)

    suspend fun cacheProducts(products: List<Product>) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(ProductCacheEntry.TABLE_NAME, null, null)
            products.forEach { product ->
                val values = ContentValues().apply {
                    put(ProductCacheEntry.COLUMN_ID, product.id)
                    put(ProductCacheEntry.COLUMN_NAME, product.name)
                    put(ProductCacheEntry.COLUMN_CATEGORY_ID, product.categoryId)
                    put(ProductCacheEntry.COLUMN_PRICE, product.displayPrice)
                    put(ProductCacheEntry.COLUMN_IMAGE_URL, product.displayImage)
                    put(ProductCacheEntry.COLUMN_DESCRIPTION, product.shortDescription.ifBlank { product.description })
                    put(ProductCacheEntry.COLUMN_RATING, product.averageRating)
                    put(ProductCacheEntry.COLUMN_REVIEW_COUNT, product.totalReviews)
                    put(ProductCacheEntry.COLUMN_TOTAL_SOLD, product.totalSold)
                    put(ProductCacheEntry.COLUMN_CACHED_AT, System.currentTimeMillis())
                }
                db.insertWithOnConflict(
                    ProductCacheEntry.TABLE_NAME,
                    null,
                    values,
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    suspend fun getCachedProducts(): List<Product> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val products = mutableListOf<Product>()
        val cursor = db.query(
            ProductCacheEntry.TABLE_NAME,
            null, null, null, null, null,
            "${ProductCacheEntry.COLUMN_TOTAL_SOLD} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val cachedPrice = it.getDouble(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_PRICE))
                val imageUrl = it.getString(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_IMAGE_URL)).orEmpty()
                products += Product(
                    id = it.getString(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_NAME)),
                    categoryId = it.getString(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_CATEGORY_ID)),
                    description = it.getString(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_DESCRIPTION)).orEmpty(),
                    images = listOfNotNull(imageUrl.takeIf { url -> url.isNotEmpty() }),
                    thumbnail = imageUrl,
                    variants = listOf(ProductVariant(id = "cached", name = "Default", price = cachedPrice)),
                    averageRating = it.getDouble(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_RATING)),
                    totalReviews = it.getInt(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_REVIEW_COUNT)),
                    totalSold = it.getInt(it.getColumnIndexOrThrow(ProductCacheEntry.COLUMN_TOTAL_SOLD))
                )
            }
        }
        products
    }

    suspend fun cacheCategories(categories: List<Category>) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(CategoryCacheEntry.TABLE_NAME, null, null)
            categories.forEach { category ->
                val values = ContentValues().apply {
                    put(CategoryCacheEntry.COLUMN_ID, category.id)
                    put(CategoryCacheEntry.COLUMN_NAME, category.name)
                    put(CategoryCacheEntry.COLUMN_ICON_URL, category.image)
                    put(CategoryCacheEntry.COLUMN_SORT_ORDER, category.sortOrder)
                }
                db.insertWithOnConflict(
                    CategoryCacheEntry.TABLE_NAME,
                    null,
                    values,
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    suspend fun getCachedCategories(): List<Category> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val categories = mutableListOf<Category>()
        val cursor = db.query(
            CategoryCacheEntry.TABLE_NAME,
            null, null, null, null, null,
            "${CategoryCacheEntry.COLUMN_SORT_ORDER} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                categories += Category(
                    id = it.getString(it.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_NAME)),
                    image = it.getString(it.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_ICON_URL)).orEmpty(),
                    sortOrder = it.getInt(it.getColumnIndexOrThrow(CategoryCacheEntry.COLUMN_SORT_ORDER))
                )
            }
        }
        categories
    }
}
