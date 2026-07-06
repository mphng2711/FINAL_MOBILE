package com.example.purepawapp.di

import android.content.Context
import com.example.purepawapp.data.local.ProductCacheDao
import com.example.purepawapp.data.repository.AuthRepository
import com.example.purepawapp.data.repository.AuthRepositoryImpl
import com.example.purepawapp.data.repository.BlogRepository
import com.example.purepawapp.data.repository.BlogRepositoryImpl
import com.example.purepawapp.data.repository.CartRepository
import com.example.purepawapp.data.repository.CartRepositoryImpl
import com.example.purepawapp.data.repository.OrderRepository
import com.example.purepawapp.data.repository.OrderRepositoryImpl
import com.example.purepawapp.data.repository.ProductRepository
import com.example.purepawapp.data.repository.ProductRepositoryImpl
import com.example.purepawapp.data.repository.ProfileRepository
import com.example.purepawapp.data.repository.ProfileRepositoryImpl
import com.example.purepawapp.data.repository.PromotionRepository
import com.example.purepawapp.data.repository.PromotionRepositoryImpl
import com.example.purepawapp.data.repository.SpaRepository
import com.example.purepawapp.data.repository.SpaRepositoryImpl
import com.example.purepawapp.data.session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object ServiceLocator {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val firebaseStorage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    val sessionManager: SessionManager by lazy { SessionManager(appContext) }
    val productCacheDao: ProductCacheDao by lazy { ProductCacheDao(appContext) }

    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(firebaseAuth, firestore) }
    val productRepository: ProductRepository by lazy { ProductRepositoryImpl(firestore, productCacheDao) }
    val cartRepository: CartRepository by lazy { CartRepositoryImpl(firestore) }
    val orderRepository: OrderRepository by lazy { OrderRepositoryImpl(firestore) }
    val spaRepository: SpaRepository by lazy { SpaRepositoryImpl(firestore) }
    val blogRepository: BlogRepository by lazy { BlogRepositoryImpl(firestore) }
    val profileRepository: ProfileRepository by lazy { ProfileRepositoryImpl(firestore) }
    val promotionRepository: PromotionRepository by lazy { PromotionRepositoryImpl(firestore) }
}
