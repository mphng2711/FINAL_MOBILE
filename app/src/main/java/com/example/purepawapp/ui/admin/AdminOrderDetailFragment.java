package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.OrderItem;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminOrderDetailBinding;
import com.example.purepawapp.databinding.ItemAdminOrderLineBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderDetailFragment extends BaseFragment<FragmentAdminOrderDetailBinding> {

    private String orderId;

    public AdminOrderDetailFragment() {
        super(FragmentAdminOrderDetailBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderId = requireArguments().getString("orderId", "");
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        loadOrder();
    }

    private void loadOrder() {
        ServiceLocator.getOrderRepository().getOrder(orderId, new RepoCallback<>() {
            @Override
            public void onSuccess(Order order) {
                bind(order);
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminOrderDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải đơn hàng");
            }
        });
    }

    private void bind(Order order) {
        String orderCode = order.getOrderCode();
        getBinding().tvOrderCode.setText(orderCode != null && !orderCode.isBlank() ? orderCode : "#" + order.getId().substring(0, Math.min(8, order.getId().length())));

        StatusStyle style = AdminStatusUi.orderStatusStyle(order.getStatus());
        getBinding().tvStatus.setText(style.getLabel());
        getBinding().tvStatus.setTextColor(getResources().getColor(style.getTextColorRes(), null));
        getBinding().tvStatus.setBackgroundTintList(getResources().getColorStateList(style.getBgColorRes(), null));

        getBinding().llOrderItems.removeAllViews();
        for (OrderItem item : order.getItems()) {
            ItemAdminOrderLineBinding lineBinding = ItemAdminOrderLineBinding.inflate(LayoutInflater.from(requireContext()), getBinding().llOrderItems, false);
            lineBinding.tvLineName.setText(item.getProductName() + " x" + item.getQuantity());
            lineBinding.tvLineSubtotal.setText(CurrencyUtils.toVndString(item.getSubtotal()));
            getBinding().llOrderItems.addView(lineBinding.getRoot());
        }

        var address = order.getShippingAddress();
        getBinding().tvShippingName.setText(address.getFullName() + " · " + address.getPhone());
        List<String> parts = new ArrayList<>();
        for (String part : List.of(address.getStreet(), address.getWard(), address.getDistrict(), address.getCity())) {
            if (part != null && !part.isBlank()) parts.add(part);
        }
        getBinding().tvShippingAddress.setText(String.join(", ", parts));

        String paymentMethod = order.getPaymentMethod();
        getBinding().tvPaymentMethod.setText(paymentMethod == null || paymentMethod.isBlank() ? "COD" : paymentMethod);
        getBinding().tvTotal.setText(CurrencyUtils.toVndString(order.getTotalAmount()));

        getBinding().chipGroupStatus.removeAllViews();
        for (String status : AdminStatusUi.ORDER_STATUS_FLOW) {
            Chip chip = new Chip(requireContext());
            chip.setText(AdminStatusUi.orderStatusStyle(status).getLabel());
            chip.setCheckable(true);
            chip.setChecked(status.equals(order.getStatus()));
            chip.setOnClickListener(v -> updateStatus(status));
            getBinding().chipGroupStatus.addView(chip);
        }
    }

    private void updateStatus(String status) {
        ServiceLocator.getOrderRepository().updateOrderStatus(orderId, status, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                ViewUtils.toast(AdminOrderDetailFragment.this, "Đã cập nhật trạng thái");
                loadOrder();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminOrderDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể cập nhật trạng thái");
            }
        });
    }
}
