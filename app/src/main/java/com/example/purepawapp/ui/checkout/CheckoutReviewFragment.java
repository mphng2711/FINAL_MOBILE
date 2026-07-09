package com.example.purepawapp.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Address;
import com.example.purepawapp.data.model.CartItem;
import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.OrderItem;
import com.example.purepawapp.data.model.OrderStatusEvent;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentCheckoutReviewBinding;
import com.example.purepawapp.databinding.ItemCheckoutReviewProductBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.ui.product.ProductUi;
import com.example.purepawapp.util.CheckoutConfig;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.OrderStatus;
import com.example.purepawapp.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckoutReviewFragment extends BaseFragment<FragmentCheckoutReviewBinding> {

    public CheckoutReviewFragment() {
        super(FragmentCheckoutReviewBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        render();

        getBinding().btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void render() {
        var cart = ServiceLocator.getCartRepository();
        List<CartItem> items = cart.getItems();

        getBinding().tvItemsTitle.setText("Sản phẩm (" + items.size() + ")");
        getBinding().llReviewItems.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            ItemCheckoutReviewProductBinding rowBinding = ItemCheckoutReviewProductBinding.inflate(
                    LayoutInflater.from(requireContext()), getBinding().llReviewItems, false);
            ProductUi.loadImage(rowBinding.ivProductImage, item.getProduct().getDisplayImage());
            rowBinding.tvName.setText(item.getProduct().getName());
            rowBinding.tvSubtitle.setText(item.getVariant().getName() + " · SL: " + item.getQuantity());
            rowBinding.tvPrice.setText(CurrencyUtils.toVndString(item.getLineTotal()));
            rowBinding.divider.setVisibility(i == items.size() - 1 ? View.GONE : View.VISIBLE);
            getBinding().llReviewItems.addView(rowBinding.getRoot());
        }

        Address address = ServiceLocator.getCheckoutDraft().getAddress();
        if (address != null) {
            getBinding().tvAddressName.setText(address.getFullName() + " · " + address.getPhone());
            List<String> parts = new ArrayList<>();
            for (String part : new String[]{address.getStreet(), address.getWard(), address.getDistrict(), address.getCity()}) {
                if (part != null && !part.isBlank()) parts.add(part);
            }
            getBinding().tvAddressDetail.setText(String.join(", ", parts));
        } else {
            getBinding().tvAddressName.setText("Chưa có địa chỉ giao hàng");
            getBinding().tvAddressDetail.setText("Vui lòng quay lại bước trước để nhập địa chỉ");
        }

        String paymentCode = ServiceLocator.getCheckoutDraft().getPaymentMethodCode();
        String paymentLabel = ServiceLocator.getCheckoutDraft().getPaymentMethodLabel();
        String icon = "bank".equals(paymentCode) ? "🏦" : "ewallet".equals(paymentCode) ? "📱" : "💵";
        getBinding().tvPaymentIcon.setText(icon);
        getBinding().tvPaymentLabel.setText(paymentLabel);

        double subtotal = cart.subtotal();
        double discount = cart.discount();
        double shipping = items.isEmpty() ? 0.0 : CheckoutConfig.SHIPPING_FEE;
        double total = Math.max(subtotal - discount, 0.0) + shipping;

        getBinding().tvSubtotal.setText(CurrencyUtils.toVndString(subtotal));
        getBinding().tvShipping.setText(CurrencyUtils.toVndString(shipping));
        getBinding().tvDiscount.setText("-" + CurrencyUtils.toVndString(discount));
        getBinding().tvTotal.setText(CurrencyUtils.toVndString(total));
    }

    private void placeOrder() {
        var cart = ServiceLocator.getCartRepository();
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            ViewUtils.toast(this, "Giỏ hàng của bạn đang trống");
            return;
        }

        Address address = ServiceLocator.getCheckoutDraft().getAddress();
        if (address == null) {
            ViewUtils.toast(this, "Vui lòng nhập địa chỉ giao hàng");
            NavHostFragment.findNavController(this).popBackStack(R.id.checkoutAddressFragment, false);
            return;
        }

        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) {
            ViewUtils.toast(this, "Vui lòng đăng nhập để đặt hàng");
            return;
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProduct().getId());
            orderItem.setProductName(item.getProduct().getName());
            orderItem.setVariantId(item.getVariant().getId());
            orderItem.setVariantName(item.getVariant().getName());
            orderItem.setImageUrl(item.getProduct().getDisplayImage());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getUnitPrice());
            orderItem.setSubtotal(item.getLineTotal());
            orderItems.add(orderItem);
        }

        double subtotal = cart.subtotal();
        double discount = cart.discount();
        double shipping = CheckoutConfig.SHIPPING_FEE;
        double total = Math.max(subtotal - discount, 0.0) + shipping;

        Order order = new Order();
        order.setOrderCode("DH" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setShippingAddress(address);
        var promo = cart.getAppliedPromotion();
        order.setCouponCode(promo != null ? promo.getCode() : "");
        order.setSubtotal(subtotal);
        order.setDiscountAmount(discount);
        order.setShippingFee(shipping);
        order.setTotalAmount(total);
        order.setPaymentMethod(ServiceLocator.getCheckoutDraft().getPaymentMethodCode());
        order.setStatus(OrderStatus.PENDING);
        order.setStatusHistory(List.of(new OrderStatusEvent(OrderStatus.PENDING, System.currentTimeMillis())));

        getBinding().btnPlaceOrder.setEnabled(false);
        showLoading();
        ServiceLocator.getOrderRepository().placeOrder(order, new RepoCallback<>() {
            @Override
            public void onSuccess(String orderId) {
                hideLoading();
                ServiceLocator.getCartRepository().clearCart();
                ServiceLocator.getCheckoutDraft().reset();
                NavHostFragment.findNavController(CheckoutReviewFragment.this)
                        .navigate(R.id.action_checkoutReviewFragment_to_orderSuccessFragment);
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                if (getBinding() != null) getBinding().btnPlaceOrder.setEnabled(true);
                ViewUtils.toast(CheckoutReviewFragment.this,
                        error.getMessage() != null ? error.getMessage() : "Không thể đặt hàng, vui lòng thử lại");
            }
        });
    }
}
