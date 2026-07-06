package com.example.purepawapp.ui.product;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.ProductVariant;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentProductDetailBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;

public class ProductDetailFragment extends BaseFragment<FragmentProductDetailBinding> {

    private int quantity = 1;
    private Product product;

    public ProductDetailFragment() {
        super(FragmentProductDetailBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String productId = getArguments() != null ? getArguments().getString("productId") : null;

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnQuantityMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                getBinding().tvQuantity.setText(String.valueOf(quantity));
            }
        });

        getBinding().btnQuantityPlus.setOnClickListener(v -> {
            quantity++;
            getBinding().tvQuantity.setText(String.valueOf(quantity));
        });

        getBinding().btnAddToCart.setOnClickListener(v -> {
            if (product == null) {
                ViewUtils.toast(this, "Không thể thêm sản phẩm vào giỏ hàng");
                return;
            }
            ProductVariant variant = product.getDefaultVariant();
            if (variant == null) {
                ViewUtils.toast(this, "Sản phẩm hiện không có sẵn");
                return;
            }
            ServiceLocator.getCartRepository().addToCart(product, variant, quantity);
            int itemCount = 0;
            for (var item : ServiceLocator.getCartRepository().getItems()) itemCount += item.getQuantity();
            ViewUtils.toast(this, "Đã thêm vào giỏ hàng (" + itemCount + " sản phẩm)");
        });

        if (productId == null || productId.isBlank()) {
            ViewUtils.toast(this, "Không thể tải sản phẩm");
            getBinding().btnAddToCart.setEnabled(false);
            return;
        }

        getBinding().btnAddToCart.setEnabled(false);
        showLoading();
        ServiceLocator.getProductRepository().getProduct(productId, new RepoCallback<>() {
            @Override
            public void onSuccess(Product fetched) {
                product = fetched;
                getBinding().tvProductImage.setText(ProductUi.getEmoji(fetched));
                getBinding().tvProductName.setText(fetched.getName());
                getBinding().tvProductSubtitle.setText(fetched.getShortDescription());
                getBinding().tvProductPrice.setText(CurrencyUtils.toVndString(fetched.getDisplayPrice()));
                getBinding().tvProductRating.setText(String.format("⭐ %.1f (%d đánh giá)", fetched.getAverageRating(), fetched.getTotalReviews()));
                getBinding().tvProductDescription.setText(fetched.getDescription());
                getBinding().btnAddToCart.setEnabled(true);
                hideLoading();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(ProductDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải sản phẩm");
                hideLoading();
            }
        });
    }
}
