package com.example.purepawapp.ui.product;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentProductListBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;

import java.util.List;

public class ProductListFragment extends BaseFragment<FragmentProductListBinding> {

    private static final long SEARCH_DEBOUNCE_MS = 350L;

    private final ProductRowAdapter adapter = new ProductRowAdapter(product -> {
        Bundle args = new Bundle();
        args.putString("productId", product.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_global_to_productDetailFragment, args);
    });

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;
    private String currentQuery = "";

    public ProductListFragment() {
        super(FragmentProductListBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvProducts.setAdapter(adapter);
        getBinding().rvProducts.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        getBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString().trim();
                if (pendingSearch != null) searchHandler.removeCallbacks(pendingSearch);
                pendingSearch = ProductListFragment.this::loadProducts;
                searchHandler.postDelayed(pendingSearch, SEARCH_DEBOUNCE_MS);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        loadProducts();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentQuery.isEmpty()) loadProducts();
    }

    @Override
    public void onDestroyView() {
        if (pendingSearch != null) searchHandler.removeCallbacks(pendingSearch);
        super.onDestroyView();
    }

    private void loadProducts() {
        String categoryId = getArguments() != null ? getArguments().getString("categoryId") : null;

        showLoading();
        RepoCallback<List<Product>> callback = new RepoCallback<>() {
            @Override
            public void onSuccess(List<Product> products) {
                if (getBinding() == null) return;
                adapter.submitList(products);
                getBinding().emptyState.setText(currentQuery.isEmpty()
                        ? "Không tìm thấy sản phẩm nào"
                        : "Không có sản phẩm phù hợp với \"" + currentQuery + "\"");
                getBinding().emptyState.setVisibility(products.isEmpty() ? View.VISIBLE : View.GONE);
                hideLoading();
            }

            @Override
            public void onError(Exception error) {
                if (getBinding() == null) return;
                ViewUtils.toast(ProductListFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách sản phẩm");
                hideLoading();
            }
        };

        if (currentQuery.isEmpty()) {
            ServiceLocator.getProductRepository().getProducts(categoryId, callback);
        } else {
            ServiceLocator.getProductRepository().searchProducts(currentQuery, callback);
        }
    }
}
