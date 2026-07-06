package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.Promotion;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminAddPromotionBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.DiscountType;
import com.example.purepawapp.util.ViewUtils;

public class AdminAddPromotionFragment extends BaseFragment<FragmentAdminAddPromotionBinding> {

    private static final long THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000;

    public AdminAddPromotionFragment() {
        super(FragmentAdminAddPromotionBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSave.setOnClickListener(v -> {
            String code = text(getBinding().etCode).trim();
            String description = text(getBinding().etDescription).trim();
            Double value = toDoubleOrNull(text(getBinding().etValue));
            Double minOrder = toDoubleOrNull(text(getBinding().etMinOrder));
            Integer usageLimit = toIntOrNull(text(getBinding().etUsageLimit));
            String discountType = getBinding().chipGroupDiscountType.getCheckedChipId() == getBinding().chipFixedAmount.getId()
                    ? DiscountType.FIXED_AMOUNT
                    : DiscountType.PERCENTAGE;

            if (code.isEmpty() || value == null) {
                ViewUtils.toast(this, "Vui lòng nhập mã và giá trị giảm giá");
                return;
            }

            getBinding().btnSave.setEnabled(false);
            long now = System.currentTimeMillis();
            Promotion promotion = new Promotion();
            promotion.setCode(code);
            promotion.setDescription(description);
            promotion.setDiscountType(discountType);
            promotion.setDiscountValue(value);
            promotion.setMinOrderAmount(minOrder != null ? minOrder : 0.0);
            promotion.setUsageLimit(usageLimit != null ? usageLimit : 0);
            promotion.setStartDate(now);
            promotion.setEndDate(now + THIRTY_DAYS_MS);
            promotion.setActive(true);

            ServiceLocator.getPromotionRepository().addPromotion(promotion, new RepoCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    ViewUtils.toast(AdminAddPromotionFragment.this, "Đã thêm mã khuyến mãi");
                    NavHostFragment.findNavController(AdminAddPromotionFragment.this).popBackStack();
                }

                @Override
                public void onError(Exception error) {
                    getBinding().btnSave.setEnabled(true);
                    ViewUtils.toast(AdminAddPromotionFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể thêm mã khuyến mãi");
                }
            });
        });
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }

    private Double toDoubleOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer toIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
