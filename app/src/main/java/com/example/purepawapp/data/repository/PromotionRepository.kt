package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.Promotion
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface PromotionRepository {
    suspend fun getPromotions(): Result<List<Promotion>>
    suspend fun addPromotion(promotion: Promotion): Result<Unit>
    suspend fun deletePromotion(code: String): Result<Unit>
}

class PromotionRepositoryImpl(
    private val firestore: FirebaseFirestore
) : PromotionRepository {

    override suspend fun getPromotions(): Result<List<Promotion>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.PROMOTIONS).get().await()
        snapshot.toObjects(Promotion::class.java)
    }

    override suspend fun addPromotion(promotion: Promotion): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.PROMOTIONS)
            .document(promotion.code.uppercase())
            .set(promotion.copy(code = promotion.code.uppercase()))
            .await()
        Unit
    }

    override suspend fun deletePromotion(code: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.PROMOTIONS).document(code.uppercase()).delete().await()
        Unit
    }
}
