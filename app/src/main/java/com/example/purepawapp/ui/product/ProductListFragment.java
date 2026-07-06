package com.example.purepawapp.ui.product;

import android.os.Bundle;
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

    private final ProductRowAdapter adapter = new ProductRowAdapter(product -> {
        Bundle args = new Bundle();
        args.putString("productId", product.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_global_to_productDetailFragment, args);
    });

    public ProductListFragment() {
        super(FragmentProductListBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvProducts.setAdapter(adapter);
        getBinding().rvProducts.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        loadProducts();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        String categoryId = getArguments() != null ? getArguments().getString("categoryId") : null;

        showLoading();
        ServiceLocator.getProductRepository().getProducts(categoryId, new RepoCallback<>() {
            @Override
            public void onSuccess(List<Product> products) {
                adapter.submitList(products);
                getBinding().emptyState.setVisibility(products.isEmpty() ? View.VISIBLE : View.GONE);
                hideLoading();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(ProductListFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách sản phẩm");
                hideLoading();
            }
        });
    }
}
