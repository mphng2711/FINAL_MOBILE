package com.example.purepawapp.ui.cart;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.repository.CartRepository;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentCartBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CheckoutConfig;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;

public class CartFragment extends BaseFragment<FragmentCartBinding> {

    private CartRepository cartRepository;

    private final CartAdapter adapter = new CartAdapter(
            item -> ServiceLocator.getCartRepository().updateQuantity(item.getProduct().getId(), item.getVariant().getId(), item.getQuantity() + 1),
            item -> ServiceLocator.getCartRepository().updateQuantity(item.getProduct().getId(), item.getVariant().getId(), item.getQuantity() - 1),
            item -> ServiceLocator.getCartRepository().removeFromCart(item.getProduct().getId(), item.getVariant().getId())
    );

    public CartFragment() {
        super(FragmentCartBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartRepository = ServiceLocator.getCartRepository();

        getBinding().rvCartItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvCartItems.setAdapter(adapter);

        getBinding().btnApplyPromo.setOnClickListener(v -> {
            String code = getBinding().etPromoCode.getText() != null ? getBinding().etPromoCode.getText().toString().trim() : "";
            if (code.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập mã giảm giá");
                return;
            }
            showLoading();
            cartRepository.applyPromoCode(code, new RepoCallback<>() {
                @Override
                public void onSuccess(com.example.purepawapp.data.model.Promotion result) {
                    ViewUtils.toast(CartFragment.this, "Áp dụng mã giảm giá thành công");
                    hideLoading();
                }

                @Override
                public void onError(Exception error) {
                    ViewUtils.toast(CartFragment.this, error.getMessage() != null ? error.getMessage() : "Mã giảm giá không hợp lệ");
                    hideLoading();
                }
            });
        });

        getBinding().btnCheckout.setOnClickListener(v -> {
            if (cartRepository.getItems().isEmpty()) {
                ViewUtils.toast(this, "Giỏ hàng của bạn đang trống");
                return;
            }
            NavHostFragment.findNavController(this).navigate(R.id.action_cartFragment_to_checkoutAddressFragment);
        });

        cartRepository.getItemsLiveData().observe(getViewLifecycleOwner(), items -> renderCart());
        cartRepository.getAppliedPromotionLiveData().observe(getViewLifecycleOwner(), promo -> renderCart());
    }

    private void renderCart() {
        var items = cartRepository.getItems();
        adapter.submitList(items);

        boolean isEmpty = items.isEmpty();
        getBinding().emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        getBinding().cartContent.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        double subtotal = cartRepository.subtotal();
        double discount = cartRepository.discount();
        double shipping = isEmpty ? 0.0 : CheckoutConfig.SHIPPING_FEE;
        double total = cartRepository.total() + shipping;

        getBinding().tvSubtotal.setText(CurrencyUtils.toVndString(subtotal));
        getBinding().tvShipping.setText(CurrencyUtils.toVndString(shipping));
        getBinding().tvDiscount.setText("-" + CurrencyUtils.toVndString(discount));
        getBinding().tvTotal.setText(CurrencyUtils.toVndString(total));
        getBinding().tvTotalBottom.setText(CurrencyUtils.toVndString(total));
        getBinding().btnCheckout.setEnabled(!isEmpty);
    }
}
