package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminCategoriesBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminCategoriesFragment extends BaseFragment<FragmentAdminCategoriesBinding> {

    private List<CategoryRow> allRows = List.of();

    private final AdminCategoryAdapter adapter = new AdminCategoryAdapter(
            this::updateCategoryActive,
            category -> {
                Bundle args = new Bundle();
                args.putString("categoryId", category.getId());
                args.putString("categoryName", category.getName());
                args.putString("categoryIcon", AdminStatusUi.categoryEmoji(category));
                NavHostFragment.findNavController(this).navigate(R.id.action_adminCategoriesFragment_to_adminAddCategoryFragment, args);
            },
            category -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Xóa danh mục")
                    .setMessage("Bạn có chắc muốn xóa danh mục \"" + category.getName() + "\"?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, which) -> deleteCategory(category.getId()))
                    .show()
    );

    public AdminCategoriesFragment() {
        super(FragmentAdminCategoriesBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        getBinding().rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvCategories.setAdapter(adapter);
        getBinding().rvCategories.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        getBinding().btnAddCategory.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminCategoriesFragment_to_adminAddCategoryFragment));

        getBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter(s == null ? "" : s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loadCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories();
    }

    private void loadCategories() {
        ServiceLocator.getProductRepository().getCategories(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Category> categories) {
                ServiceLocator.getProductRepository().getProducts(null, new RepoCallback<>() {
                    @Override
                    public void onSuccess(List<Product> products) {
                        buildRows(categories, products);
                    }

                    @Override
                    public void onError(Exception error) {
                        buildRows(categories, List.of());
                    }
                });
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminCategoriesFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh mục");
            }
        });
    }

    private void buildRows(List<Category> categories, List<Product> products) {
        List<Category> sorted = new ArrayList<>(categories);
        sorted.sort(Comparator.comparingInt(Category::getSortOrder));
        List<CategoryRow> rows = new ArrayList<>();
        for (Category category : sorted) {
            int count = 0;
            for (Product p : products) if (category.getId().equals(p.getCategoryId())) count++;
            rows.add(new CategoryRow(category, count));
        }
        allRows = rows;
        String query = getBinding().etSearch.getText() != null ? getBinding().etSearch.getText().toString() : "";
        applyFilter(query);
    }

    private void applyFilter(String query) {
        List<CategoryRow> filtered;
        if (query.isBlank()) {
            filtered = allRows;
        } else {
            filtered = new ArrayList<>();
            for (CategoryRow row : allRows) {
                if (row.getCategory().getName().toLowerCase().contains(query.toLowerCase())) filtered.add(row);
            }
        }
        adapter.submitList(filtered);
        getBinding().emptyState.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateCategoryActive(Category category, boolean isActive) {
        category.setActive(isActive);
        ServiceLocator.getProductRepository().updateCategory(category, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminCategoriesFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể cập nhật danh mục");
                loadCategories();
            }
        });
    }

    private void deleteCategory(String categoryId) {
        ServiceLocator.getProductRepository().deleteCategory(categoryId, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                ViewUtils.toast(AdminCategoriesFragment.this, "Đã xóa danh mục");
                loadCategories();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminCategoriesFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể xóa danh mục");
            }
        });
    }
}
