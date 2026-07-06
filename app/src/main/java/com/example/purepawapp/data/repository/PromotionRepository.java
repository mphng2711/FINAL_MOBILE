package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Promotion;

import java.util.List;

public interface PromotionRepository {
    void getPromotions(RepoCallback<List<Promotion>> callback);

    void addPromotion(Promotion promotion, RepoCallback<Void> callback);

    void deletePromotion(String code, RepoCallback<Void> callback);
}
