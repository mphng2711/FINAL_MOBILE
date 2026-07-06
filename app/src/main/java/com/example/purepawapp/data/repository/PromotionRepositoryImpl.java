package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Promotion;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PromotionRepositoryImpl implements PromotionRepository {

    private final FirebaseFirestore firestore;

    public PromotionRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void getPromotions(RepoCallback<List<Promotion>> callback) {
        firestore.collection(FirestoreCollections.PROMOTIONS).get()
                .addOnSuccessListener(snapshot -> callback.onSuccess(snapshot.toObjects(Promotion.class)))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void addPromotion(Promotion promotion, RepoCallback<Void> callback) {
        String code = promotion.getCode().toUpperCase();
        promotion.setCode(code);
        firestore.collection(FirestoreCollections.PROMOTIONS).document(code).set(promotion)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void deletePromotion(String code, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.PROMOTIONS).document(code.toUpperCase()).delete()
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }
}
