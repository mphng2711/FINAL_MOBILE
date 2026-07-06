package com.example.purepawapp.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.R;
import com.example.purepawapp.data.repository.CartRepository;
import com.example.purepawapp.databinding.FragmentHomeBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.ui.product.ProductCardAdapter;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;

import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private final ProductCardAdapter adapter = new ProductCardAdapter(
            product -> {
                Bundle args = new Bundle();
                args.putString("productId", product.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_global_to_productDetailFragment, args);
            },
            product -> {
                CartRepository cartRepository = ServiceLocator.getCartRepository();
                var variant = product.getDefaultVariant();
                if (variant == null) {
                    ViewUtils.toast(this, "Sản phẩm hiện không có sẵn");
                } else {
                    cartRepository.addToCart(product, variant, 1);
                    int itemCount = 0;
                    for (var item : cartRepository.getItems()) itemCount += item.getQuantity();
                    ViewUtils.toast(this, "Đã thêm vào giỏ hàng (" + itemCount + " sản phẩm)");
                }
            }
    );

    public HomeFragment() {
        super(FragmentHomeBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        getBinding().rvFeaturedProducts.setAdapter(adapter);
        getBinding().rvFeaturedProducts.addItemDecoration(
                new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density), true));

        getBinding().btnShopNow.setOnClickListener(v -> goToProducts(null));
        getBinding().tvSeeAllProducts.setOnClickListener(v -> goToProducts(null));

        getBinding().cardCategoryFood.setOnClickListener(v -> goToProducts("food"));
        getBinding().cardCategoryAccessories.setOnClickListener(v -> goToProducts("accessories"));
        getBinding().cardCategoryHealth.setOnClickListener(v -> goToProducts("health"));
        getBinding().cardCategorySpa.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.spa_nav_graph));

        loadFeaturedProducts();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFeaturedProducts();
    }

    private void loadFeaturedProducts() {
        showLoading();
        ServiceLocator.getProductRepository().getProducts(null, new com.example.purepawapp.data.repository.RepoCallback<>() {
            @Override
            public void onSuccess(List<com.example.purepawapp.data.model.Product> products) {
                List<com.example.purepawapp.data.model.Product> featured = products.stream()
                        .filter(com.example.purepawapp.data.model.Product::isFeatured)
                        .collect(Collectors.toList());
                if (featured.isEmpty()) featured = products;
                if (featured.size() > 6) featured = featured.subList(0, 6);
                adapter.submitList(featured);
                getBinding().tvFeaturedEmpty.setVisibility(featured.isEmpty() ? View.VISIBLE : View.GONE);
                hideLoading();
            }

            @Override
            public void onError(Exception error) {
                getBinding().tvFeaturedEmpty.setVisibility(View.VISIBLE);
                hideLoading();
            }
        });
    }

    private void goToProducts(String categoryId) {
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        NavHostFragment.findNavController(this).navigate(R.id.product_nav_graph, args);
    }
}
