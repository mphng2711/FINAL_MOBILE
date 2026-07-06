package com.example.purepawapp.di;

import android.content.Context;

import com.example.purepawapp.data.local.ProductCacheDao;
import com.example.purepawapp.data.repository.AuthRepository;
import com.example.purepawapp.data.repository.AuthRepositoryImpl;
import com.example.purepawapp.data.repository.BlogRepository;
import com.example.purepawapp.data.repository.BlogRepositoryImpl;
import com.example.purepawapp.data.repository.CartRepository;
import com.example.purepawapp.data.repository.CartRepositoryImpl;
import com.example.purepawapp.data.repository.OrderRepository;
import com.example.purepawapp.data.repository.OrderRepositoryImpl;
import com.example.purepawapp.data.repository.ProductRepository;
import com.example.purepawapp.data.repository.ProductRepositoryImpl;
import com.example.purepawapp.data.repository.ProfileRepository;
import com.example.purepawapp.data.repository.ProfileRepositoryImpl;
import com.example.purepawapp.data.repository.PromotionRepository;
import com.example.purepawapp.data.repository.PromotionRepositoryImpl;
import com.example.purepawapp.data.repository.SpaRepository;
import com.example.purepawapp.data.repository.SpaRepositoryImpl;
import com.example.purepawapp.data.session.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public final class ServiceLocator {

    private static Context appContext;

    private static FirebaseAuth firebaseAuth;
    private static FirebaseFirestore firestore;
    private static FirebaseStorage firebaseStorage;
    private static SessionManager sessionManager;
    private static ProductCacheDao productCacheDao;
    private static AuthRepository authRepository;
    private static ProductRepository productRepository;
    private static CartRepository cartRepository;
    private static OrderRepository orderRepository;
    private static SpaRepository spaRepository;
    private static BlogRepository blogRepository;
    private static ProfileRepository profileRepository;
    private static PromotionRepository promotionRepository;

    private ServiceLocator() {
    }

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth;
    }

    public static FirebaseFirestore getFirestore() {
        if (firestore == null) firestore = FirebaseFirestore.getInstance();
        return firestore;
    }

    public static FirebaseStorage getFirebaseStorage() {
        if (firebaseStorage == null) firebaseStorage = FirebaseStorage.getInstance();
        return firebaseStorage;
    }

    public static SessionManager getSessionManager() {
        if (sessionManager == null) sessionManager = new SessionManager(appContext);
        return sessionManager;
    }

    public static ProductCacheDao getProductCacheDao() {
        if (productCacheDao == null) productCacheDao = new ProductCacheDao(appContext);
        return productCacheDao;
    }

    public static AuthRepository getAuthRepository() {
        if (authRepository == null) authRepository = new AuthRepositoryImpl(getFirebaseAuth(), getFirestore());
        return authRepository;
    }

    public static ProductRepository getProductRepository() {
        if (productRepository == null) productRepository = new ProductRepositoryImpl(getFirestore(), getProductCacheDao());
        return productRepository;
    }

    public static CartRepository getCartRepository() {
        if (cartRepository == null) cartRepository = new CartRepositoryImpl(getFirestore());
        return cartRepository;
    }

    public static OrderRepository getOrderRepository() {
        if (orderRepository == null) orderRepository = new OrderRepositoryImpl(getFirestore());
        return orderRepository;
    }

    public static SpaRepository getSpaRepository() {
        if (spaRepository == null) spaRepository = new SpaRepositoryImpl(getFirestore());
        return spaRepository;
    }

    public static BlogRepository getBlogRepository() {
        if (blogRepository == null) blogRepository = new BlogRepositoryImpl(getFirestore());
        return blogRepository;
    }

    public static ProfileRepository getProfileRepository() {
        if (profileRepository == null) profileRepository = new ProfileRepositoryImpl(getFirestore());
        return profileRepository;
    }

    public static PromotionRepository getPromotionRepository() {
        if (promotionRepository == null) promotionRepository = new PromotionRepositoryImpl(getFirestore());
        return promotionRepository;
    }
}
