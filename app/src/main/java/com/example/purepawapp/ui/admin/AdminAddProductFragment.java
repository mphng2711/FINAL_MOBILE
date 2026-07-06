package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminAddProductBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

import java.util.List;
import java.util.UUID;

public class AdminAddProductFragment extends BaseFragment<FragmentAdminAddProductBinding> {

    public AdminAddProductFragment() {
        super(FragmentAdminAddProductBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSave.setOnClickListener(v -> {
            String name = text(getBinding().etName).trim();
            String shortDescription = text(getBinding().etShortDescription).trim();
            String description = text(getBinding().etDescription).trim();
            Double price = toDoubleOrNull(text(getBinding().etPrice));
            Integer stock = toIntOrNull(text(getBinding().etStock));

            int checkedId = getBinding().chipGroupCategory.getCheckedChipId();
            View checkedChip = getBinding().getRoot().findViewById(checkedId);
            String categoryId = checkedChip != null && checkedChip.getTag() instanceof String
                    ? (String) checkedChip.getTag() : "food";

            if (name.isEmpty() || price == null || stock == null) {
                ViewUtils.toast(this, "Vui lòng nhập đầy đủ tên, giá và tồn kho");
                return;
            }

            getBinding().btnSave.setEnabled(false);
            Product product = new Product();
            product.setName(name);
            product.setSlug(name.toLowerCase().replaceAll("\\s+", "-"));
            product.setCategoryId(categoryId);
            product.setDescription(description);
            product.setShortDescription(shortDescription);

            ProductVariant variant = new ProductVariant();
            variant.setId(UUID.randomUUID().toString());
            variant.setName("Mặc định");
            variant.setPrice(price);
            variant.setSku("SKU-" + System.currentTimeMillis());
            variant.setStock(stock);
            product.setVariants(List.of(variant));

            ServiceLocator.getProductRepository().addProduct(product, new RepoCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    ViewUtils.toast(AdminAddProductFragment.this, "Đã thêm sản phẩm");
                    NavHostFragment.findNavController(AdminAddProductFragment.this).popBackStack();
                }

                @Override
                public void onError(Exception error) {
                    getBinding().btnSave.setEnabled(true);
                    ViewUtils.toast(AdminAddProductFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể thêm sản phẩm");
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
