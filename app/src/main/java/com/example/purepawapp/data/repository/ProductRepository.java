package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.Review;

import java.util.List;

public interface ProductRepository {
    void getCategories(RepoCallback<List<Category>> callback);

    void getProducts(String categoryId, RepoCallback<List<Product>> callback);

    void getProduct(String productId, RepoCallback<Product> callback);

    void searchProducts(String query, RepoCallback<List<Product>> callback);

    void getReviews(String productId, RepoCallback<List<Review>> callback);

    void addReview(String productId, Review review, RepoCallback<Void> callback);

    void addProduct(Product product, RepoCallback<Void> callback);

    void updateProduct(Product product, RepoCallback<Void> callback);

    void deleteProduct(String productId, RepoCallback<Void> callback);

    void addCategory(Category category, RepoCallback<Void> callback);

    void updateCategory(Category category, RepoCallback<Void> callback);

    void deleteCategory(String categoryId, RepoCallback<Void> callback);
}
