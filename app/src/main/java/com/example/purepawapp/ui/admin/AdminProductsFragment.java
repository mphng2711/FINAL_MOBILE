package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminProductsBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AdminProductsFragment extends BaseFragment<FragmentAdminProductsBinding> {

    private List<Product> allProducts = List.of();

    private final AdminProductAdapter adapter = new AdminProductAdapter(
            product -> {
                Bundle args = new Bundle();
                args.putString("productId", product.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_adminProductsFragment_to_adminAddProductFragment, args);
            },
            product -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa \"" + product.getName() + "\"?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, which) -> deleteProduct(product.getId()))
                    .show());

    public AdminProductsFragment() {
        super(FragmentAdminProductsBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvProducts.setAdapter(adapter);

        getBinding().btnAddProduct.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminProductsFragment_to_adminAddProductFragment));

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

        loadProducts();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        ServiceLocator.getProductRepository().getProducts(null, new RepoCallback<>() {
            @Override
            public void onSuccess(List<Product> result) {
                allProducts = result;
                String query = getBinding().etSearch.getText() != null ? getBinding().etSearch.getText().toString() : "";
                applyFilter(query);
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminProductsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách sản phẩm");
            }
        });
    }

    private void applyFilter(String query) {
        List<Product> filtered;
        if (query.isBlank()) {
            filtered = allProducts;
        } else {
            filtered = new ArrayList<>();
            String lower = query.toLowerCase();
            for (Product p : allProducts) if (p.getName().toLowerCase().contains(lower)) filtered.add(p);
        }
        adapter.submitList(filtered);
        getBinding().emptyState.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void deleteProduct(String productId) {
        ServiceLocator.getProductRepository().deleteProduct(productId, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                ViewUtils.toast(AdminProductsFragment.this, "Đã xóa sản phẩm");
                loadProducts();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminProductsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể xóa sản phẩm");
            }
        });
    }
}
