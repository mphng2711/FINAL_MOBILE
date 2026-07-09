package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.local.ProductCacheDao;
import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.Review;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    private final FirebaseFirestore firestore;
    private final ProductCacheDao cacheDao;

    public ProductRepositoryImpl(FirebaseFirestore firestore, ProductCacheDao cacheDao) {
        this.firestore = firestore;
        this.cacheDao = cacheDao;
    }

    @Override
    public void getCategories(RepoCallback<List<Category>> callback) {
        firestore.collection(FirestoreCollections.CATEGORIES)
                .orderBy("sortOrder")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Category> categories = snapshot.toObjects(Category.class);
                    cacheDao.cacheCategories(categories);
                    callback.onSuccess(categories);
                })
                .addOnFailureListener(e -> {
                    List<Category> cached = cacheDao.getCachedCategories();
                    if (cached.isEmpty()) {
                        callback.onError(e);
                    } else {
                        callback.onSuccess(cached);
                    }
                });
    }

    @Override
    public void getProducts(String categoryId, RepoCallback<List<Product>> callback) {
        Query query = firestore.collection(FirestoreCollections.PRODUCTS);
        boolean hasCategory = categoryId != null && !categoryId.isBlank();
        if (hasCategory) {
            query = query.whereEqualTo("categoryId", categoryId);
        }
        query.get()
                .addOnSuccessListener(snapshot -> {
                    List<Product> products = snapshot.toObjects(Product.class);
                    if (!hasCategory) cacheDao.cacheProducts(products);
                    callback.onSuccess(products);
                })
                .addOnFailureListener(e -> {
                    List<Product> cached = cacheDao.getCachedProducts();
                    if (hasCategory) {
                        List<Product> filtered = new ArrayList<>();
                        for (Product p : cached) {
                            if (categoryId.equals(p.getCategoryId())) filtered.add(p);
                        }
                        cached = filtered;
                    }
                    if (cached.isEmpty()) {
                        callback.onError(e);
                    } else {
                        callback.onSuccess(cached);
                    }
                });
    }

    @Override
    public void getProduct(String productId, RepoCallback<Product> callback) {
        firestore.collection(FirestoreCollections.PRODUCTS).document(productId).get()
                .addOnSuccessListener(doc -> {
                    Product product = doc.toObject(Product.class);
                    if (product == null) {
                        callback.onError(new IllegalStateException("Product not found"));
                    } else {
                        callback.onSuccess(product);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void searchProducts(String query, RepoCallback<List<Product>> callback) {
        firestore.collection(FirestoreCollections.PRODUCTS).get()
                .addOnSuccessListener(snapshot -> {
                    List<Product> results = new ArrayList<>();
                    for (Product p : snapshot.toObjects(Product.class)) {
                        if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                            results.add(p);
                        }
                    }
                    callback.onSuccess(results);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getReviews(String productId, RepoCallback<List<Review>> callback) {
        firestore.collection(FirestoreCollections.PRODUCTS)
                .document(productId)
                .collection(FirestoreCollections.REVIEWS)
                .orderBy("createdAt")
                .get()
                .addOnSuccessListener(snapshot -> callback.onSuccess(snapshot.toObjects(Review.class)))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void addReview(String productId, Review review, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.PRODUCTS)
                .document(productId)
                .collection(FirestoreCollections.REVIEWS)
                .add(review)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void addProduct(Product product, RepoCallback<Void> callback) {
        var docRef = firestore.collection(FirestoreCollections.PRODUCTS).document();
        product.setId(docRef.getId());
        docRef.set(product)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void updateProduct(Product product, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.PRODUCTS).document(product.getId()).set(product)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void deleteProduct(String productId, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.PRODUCTS).document(productId).delete()
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void addCategory(Category category, RepoCallback<Void> callback) {
        String id = category.getId().isBlank()
                ? firestore.collection(FirestoreCollections.CATEGORIES).document().getId()
                : category.getId();
        category.setId(id);
        firestore.collection(FirestoreCollections.CATEGORIES).document(id).set(category)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void updateCategory(Category category, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.CATEGORIES).document(category.getId()).set(category)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void deleteCategory(String categoryId, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.CATEGORIES).document(categoryId).delete()
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }
}
