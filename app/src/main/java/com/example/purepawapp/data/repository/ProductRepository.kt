package com.example.purepawapp.data.repository

import com.example.purepawapp.data.local.ProductCacheDao
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.data.model.Review
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface ProductRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getProducts(categoryId: String? = null): Result<List<Product>>
    suspend fun getProduct(productId: String): Result<Product>
    suspend fun searchProducts(query: String): Result<List<Product>>
    suspend fun getReviews(productId: String): Result<List<Review>>
    suspend fun addReview(productId: String, review: Review): Result<Unit>
    suspend fun addProduct(product: Product): Result<Unit>
    suspend fun deleteProduct(productId: String): Result<Unit>
    suspend fun addCategory(category: Category): Result<Unit>
    suspend fun updateCategory(category: Category): Result<Unit>
    suspend fun deleteCategory(categoryId: String): Result<Unit>
}

class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val cacheDao: ProductCacheDao
) : ProductRepository {

    override suspend fun getCategories(): Result<List<Category>> = runCatching {
        try {
            val snapshot = firestore.collection(FirestoreCollections.CATEGORIES)
                .orderBy("sortOrder")
                .get()
                .await()
            val categories = snapshot.toObjects(Category::class.java)
            cacheDao.cacheCategories(categories)
            categories
        } catch (e: Exception) {
            cacheDao.getCachedCategories().ifEmpty { throw e }
        }
    }

    override suspend fun getProducts(categoryId: String?): Result<List<Product>> = runCatching {
        try {
            var query: com.google.firebase.firestore.Query =
                firestore.collection(FirestoreCollections.PRODUCTS)
            if (!categoryId.isNullOrBlank()) {
                query = query.whereEqualTo("categoryId", categoryId)
            }
            val snapshot = query.get().await()
            val products = snapshot.toObjects(Product::class.java)
            if (categoryId.isNullOrBlank()) cacheDao.cacheProducts(products)
            products
        } catch (e: Exception) {
            cacheDao.getCachedProducts()
                .let { cached -> if (categoryId.isNullOrBlank()) cached else cached.filter { it.categoryId == categoryId } }
                .ifEmpty { throw e }
        }
    }

    override suspend fun getProduct(productId: String): Result<Product> = runCatching {
        val doc = firestore.collection(FirestoreCollections.PRODUCTS).document(productId).get().await()
        doc.toObject(Product::class.java) ?: error("Product not found")
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.PRODUCTS).get().await()
        snapshot.toObjects(Product::class.java).filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getReviews(productId: String): Result<List<Review>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.PRODUCTS)
            .document(productId)
            .collection(FirestoreCollections.REVIEWS)
            .orderBy("createdAt")
            .get()
            .await()
        snapshot.toObjects(Review::class.java)
    }

    override suspend fun addReview(productId: String, review: Review): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.PRODUCTS)
            .document(productId)
            .collection(FirestoreCollections.REVIEWS)
            .add(review)
            .await()
        Unit
    }

    override suspend fun addProduct(product: Product): Result<Unit> = runCatching {
        val docRef = firestore.collection(FirestoreCollections.PRODUCTS).document()
        docRef.set(product.copy(id = docRef.id)).await()
        Unit
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.PRODUCTS).document(productId).delete().await()
        Unit
    }

    override suspend fun addCategory(category: Category): Result<Unit> = runCatching {
        val id = category.id.ifBlank { firestore.collection(FirestoreCollections.CATEGORIES).document().id }
        firestore.collection(FirestoreCollections.CATEGORIES).document(id).set(category.copy(id = id)).await()
        Unit
    }

    override suspend fun updateCategory(category: Category): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.CATEGORIES).document(category.id).set(category).await()
        Unit
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.CATEGORIES).document(categoryId).delete().await()
        Unit
    }
}
