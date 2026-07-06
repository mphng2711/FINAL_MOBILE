package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Promotion;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminPromotionsBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class AdminPromotionsFragment extends BaseFragment<FragmentAdminPromotionsBinding> {

    private static final long SEVEN_DAYS_MS = 7L * 24 * 60 * 60 * 1000;

    private final AdminPromotionAdapter adapter = new AdminPromotionAdapter(
            this::toggleActive,
            promotion -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Xóa mã khuyến mãi")
                    .setMessage("Bạn có chắc muốn xóa mã \"" + promotion.getCode() + "\"?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, which) -> deletePromotion(promotion.getCode()))
                    .show()
    );

    public AdminPromotionsFragment() {
        super(FragmentAdminPromotionsBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        getBinding().rvPromotions.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvPromotions.setAdapter(adapter);
        getBinding().rvPromotions.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        getBinding().btnAddPromotion.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminPromotionsFragment_to_adminAddPromotionFragment));

        loadPromotions();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPromotions();
    }

    private void loadPromotions() {
        ServiceLocator.getPromotionRepository().getPromotions(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Promotion> promotions) {
                adapter.submitList(promotions);
                getBinding().emptyState.setVisibility(promotions.isEmpty() ? View.VISIBLE : View.GONE);

                long now = System.currentTimeMillis();
                int activeCount = 0;
                int usedCount = 0;
                int expiringCount = 0;
                for (Promotion p : promotions) {
                    if (p.isActive()) activeCount++;
                    usedCount += p.getUsedCount();
                    if (p.isActive() && p.getEndDate() >= now && p.getEndDate() <= now + SEVEN_DAYS_MS) expiringCount++;
                }
                getBinding().tvActiveCount.setText(String.valueOf(activeCount));
                getBinding().tvUsedCount.setText(String.valueOf(usedCount));
                getBinding().tvExpiringCount.setText(String.valueOf(expiringCount));
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminPromotionsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách khuyến mãi");
            }
        });
    }

    private void toggleActive(Promotion promotion, boolean isActive) {
        promotion.setActive(isActive);
        ServiceLocator.getPromotionRepository().addPromotion(promotion, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminPromotionsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể cập nhật khuyến mãi");
                loadPromotions();
            }
        });
    }

    private void deletePromotion(String code) {
        ServiceLocator.getPromotionRepository().deletePromotion(code, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                ViewUtils.toast(AdminPromotionsFragment.this, "Đã xóa mã khuyến mãi");
                loadPromotions();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminPromotionsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể xóa mã khuyến mãi");
            }
        });
    }
}
