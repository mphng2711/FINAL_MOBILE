package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminAddCategoryBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

public class AdminAddCategoryFragment extends BaseFragment<FragmentAdminAddCategoryBinding> {

    public AdminAddCategoryFragment() {
        super(FragmentAdminAddCategoryBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String categoryId = getArguments() != null ? getArguments().getString("categoryId") : null;
        boolean isEditMode = categoryId != null && !categoryId.isBlank();
        if (isEditMode) {
            getBinding().tvTitle.setText("Sửa danh mục");
            getBinding().etName.setText(getArguments().getString("categoryName", ""));
            getBinding().etIcon.setText(getArguments().getString("categoryIcon", ""));
        }

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSave.setOnClickListener(v -> {
            String name = text(getBinding().etName).trim();
            String icon = text(getBinding().etIcon).trim();

            if (name.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập tên danh mục");
                return;
            }

            getBinding().btnSave.setEnabled(false);
            Category category = new Category();
            category.setId(categoryId == null ? "" : categoryId);
            category.setName(name);
            category.setSlug(name.toLowerCase().replaceAll("\\s+", "-"));
            category.setImage(icon);
            category.setActive(true);

            RepoCallback<Void> callback = new RepoCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    ViewUtils.toast(AdminAddCategoryFragment.this, isEditMode ? "Đã cập nhật danh mục" : "Đã thêm danh mục");
                    NavHostFragment.findNavController(AdminAddCategoryFragment.this).popBackStack();
                }

                @Override
                public void onError(Exception error) {
                    getBinding().btnSave.setEnabled(true);
                    ViewUtils.toast(AdminAddCategoryFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể lưu danh mục");
                }
            };

            if (isEditMode) {
                ServiceLocator.getProductRepository().updateCategory(category, callback);
            } else {
                ServiceLocator.getProductRepository().addCategory(category, callback);
            }
        });
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }
}
