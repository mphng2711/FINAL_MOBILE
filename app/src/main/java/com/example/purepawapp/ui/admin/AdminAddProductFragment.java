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
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.UUID;

public class AdminAddProductFragment extends BaseFragment<FragmentAdminAddProductBinding> {

    private String editingProductId;
    private Product editingProduct;

    public AdminAddProductFragment() {
        super(FragmentAdminAddProductBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        editingProductId = getArguments() != null ? getArguments().getString("productId") : null;
        if (editingProductId != null && !editingProductId.isBlank()) {
            getBinding().tvTitle.setText("Sửa sản phẩm");
            getBinding().btnSave.setText("Cập nhật sản phẩm");
            loadProductToEdit(editingProductId);
        }

        getBinding().btnSave.setOnClickListener(v -> {
            String name = text(getBinding().etName).trim();
            String shortDescription = text(getBinding().etShortDescription).trim();
            String description = text(getBinding().etDescription).trim();
            String imageUrl = text(getBinding().etImageUrl).trim();
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
            boolean isEditMode = editingProduct != null;
            Product product = isEditMode ? editingProduct : new Product();
            product.setName(name);
            product.setSlug(name.toLowerCase().replaceAll("\\s+", "-"));
            product.setCategoryId(categoryId);
            product.setDescription(description);
            product.setShortDescription(shortDescription);
            if (!imageUrl.isEmpty()) {
                product.setThumbnail(imageUrl);
                product.setImages(List.of(imageUrl));
            }

            ProductVariant variant = isEditMode && product.getDefaultVariant() != null
                    ? product.getDefaultVariant()
                    : newVariant();
            variant.setPrice(price);
            variant.setStock(stock);
            product.setVariants(List.of(variant));

            RepoCallback<Void> callback = new RepoCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    ViewUtils.toast(AdminAddProductFragment.this, isEditMode ? "Đã cập nhật sản phẩm" : "Đã thêm sản phẩm");
                    NavHostFragment.findNavController(AdminAddProductFragment.this).popBackStack();
                }

                @Override
                public void onError(Exception error) {
                    getBinding().btnSave.setEnabled(true);
                    ViewUtils.toast(AdminAddProductFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể lưu sản phẩm");
                }
            };

            if (isEditMode) {
                ServiceLocator.getProductRepository().updateProduct(product, callback);
            } else {
                ServiceLocator.getProductRepository().addProduct(product, callback);
            }
        });
    }

    private void loadProductToEdit(String productId) {
        getBinding().btnSave.setEnabled(false);
        ServiceLocator.getProductRepository().getProduct(productId, new RepoCallback<>() {
            @Override
            public void onSuccess(Product product) {
                editingProduct = product;
                getBinding().btnSave.setEnabled(true);
                getBinding().etName.setText(product.getName());
                getBinding().etShortDescription.setText(product.getShortDescription());
                getBinding().etDescription.setText(product.getDescription());
                getBinding().etImageUrl.setText(product.getDisplayImage());
                getBinding().etPrice.setText(String.valueOf((long) product.getDisplayPrice()));
                ProductVariant variant = product.getDefaultVariant();
                getBinding().etStock.setText(String.valueOf(variant != null ? variant.getStock() : 0));

                for (int i = 0; i < getBinding().chipGroupCategory.getChildCount(); i++) {
                    View child = getBinding().chipGroupCategory.getChildAt(i);
                    if (child instanceof Chip && product.getCategoryId().equals(child.getTag())) {
                        ((Chip) child).setChecked(true);
                    }
                }
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminAddProductFragment.this, "Không thể tải sản phẩm cần sửa");
                NavHostFragment.findNavController(AdminAddProductFragment.this).popBackStack();
            }
        });
    }

    private ProductVariant newVariant() {
        ProductVariant variant = new ProductVariant();
        variant.setId(UUID.randomUUID().toString());
        variant.setName("Mặc định");
        variant.setSku("SKU-" + System.currentTimeMillis());
        return variant;
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
