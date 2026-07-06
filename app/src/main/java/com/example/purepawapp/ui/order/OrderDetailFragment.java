package com.example.purepawapp.ui.order;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.OrderItem;
import com.example.purepawapp.data.model.OrderStatusEvent;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentOrderDetailBinding;
import com.example.purepawapp.databinding.ItemAdminOrderLineBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.admin.AdminStatusUi;
import com.example.purepawapp.ui.admin.StatusStyle;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class OrderDetailFragment extends BaseFragment<FragmentOrderDetailBinding> {

    private String orderId;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", new Locale("vi", "VN"));

    public OrderDetailFragment() {
        super(FragmentOrderDetailBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderId = getArguments() != null ? getArguments().getString("orderId", "") : "";

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        loadOrder();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrder();
    }

    private void loadOrder() {
        if (orderId == null || orderId.isBlank()) return;
        showLoading();
        ServiceLocator.getOrderRepository().getOrder(orderId, new RepoCallback<>() {
            @Override
            public void onSuccess(Order order) {
                bind(order);
                hideLoading();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(OrderDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải đơn hàng");
                hideLoading();
            }
        });
    }

    private void bind(Order order) {
        StatusStyle style = AdminStatusUi.orderStatusStyle(order.getStatus());
        getBinding().tvHeroTitle.setText(style.getLabel());
        getBinding().tvHeroSubtitle.setText("Ngày đặt: " + dateFormat.format(order.getCreatedAt()));
        String orderCode = order.getOrderCode();
        String displayCode = orderCode != null && !orderCode.isBlank() ? orderCode : "#" + order.getId().substring(0, Math.min(8, order.getId().length()));
        getBinding().tvOrderCode.setText("Mã đơn: " + displayCode);

        renderTimeline(order);

        getBinding().llOrderItems.removeAllViews();
        for (OrderItem item : order.getItems()) {
            ItemAdminOrderLineBinding lineBinding = ItemAdminOrderLineBinding.inflate(LayoutInflater.from(requireContext()), getBinding().llOrderItems, false);
            lineBinding.tvLineName.setText(item.getProductName() + " x" + item.getQuantity());
            lineBinding.tvLineSubtotal.setText(CurrencyUtils.toVndString(item.getSubtotal()));
            getBinding().llOrderItems.addView(lineBinding.getRoot());
        }

        var address = order.getShippingAddress();
        getBinding().tvShippingName.setText(address.getFullName() + " | " + address.getPhone());
        List<String> addressParts = new ArrayList<>();
        for (String part : List.of(address.getStreet(), address.getWard(), address.getDistrict(), address.getCity())) {
            if (part != null && !part.isBlank()) addressParts.add(part);
        }
        getBinding().tvShippingAddress.setText(String.join(", ", addressParts));

        String paymentMethod = order.getPaymentMethod() == null ? "" : order.getPaymentMethod();
        String paymentLabel;
        switch (paymentMethod.toLowerCase()) {
            case "cod":
                paymentLabel = "Thanh toán khi nhận hàng (COD)";
                break;
            case "bank":
            case "banking":
                paymentLabel = "Chuyển khoản ngân hàng";
                break;
            default:
                paymentLabel = paymentMethod.isBlank() ? "Thanh toán khi nhận hàng (COD)" : paymentMethod;
        }
        getBinding().tvPaymentMethod.setText(paymentLabel);

        getBinding().tvSubtotal.setText(CurrencyUtils.toVndString(order.getSubtotal()));
        getBinding().tvShippingFee.setText(CurrencyUtils.toVndString(order.getShippingFee()));
        if (order.getDiscountAmount() > 0) {
            getBinding().rowDiscount.setVisibility(View.VISIBLE);
            getBinding().tvDiscount.setText("-" + CurrencyUtils.toVndString(order.getDiscountAmount()));
        } else {
            getBinding().rowDiscount.setVisibility(View.GONE);
        }
        getBinding().tvTotalAmount.setText(CurrencyUtils.toVndString(order.getTotalAmount()));
    }

    private void renderTimeline(Order order) {
        getBinding().llTimeline.removeAllViews();
        float density = getResources().getDisplayMetrics().density;

        List<OrderStatusEvent> history = new ArrayList<>(order.getStatusHistory());
        if (history.isEmpty()) {
            history.add(new OrderStatusEvent(order.getStatus(), order.getCreatedAt()));
        }
        history.sort(Comparator.comparingLong(OrderStatusEvent::getTimestamp).reversed());

        for (int index = 0; index < history.size(); index++) {
            OrderStatusEvent event = history.get(index);
            boolean isLast = index == history.size() - 1;

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout dotColumn = new LinearLayout(requireContext());
            dotColumn.setOrientation(LinearLayout.VERTICAL);
            dotColumn.setGravity(Gravity.CENTER_HORIZONTAL);
            dotColumn.setLayoutParams(new LinearLayout.LayoutParams(
                    (int) (12 * density),
                    isLast ? LinearLayout.LayoutParams.WRAP_CONTENT : LinearLayout.LayoutParams.MATCH_PARENT));

            View dot = new View(requireContext());
            dot.setBackground(getResources().getDrawable(R.drawable.shape_circle, null));
            dot.setBackgroundTintList(getResources().getColorStateList(
                    index == 0 ? R.color.pp_primary : R.color.pp_timeline_inactive, null));
            dot.setLayoutParams(new LinearLayout.LayoutParams((int) (12 * density), (int) (12 * density)));
            dotColumn.addView(dot);

            if (!isLast) {
                View line = new View(requireContext());
                line.setBackgroundColor(getResources().getColor(R.color.pp_timeline_inactive, null));
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams((int) (2 * density), 0, 1f);
                line.setLayoutParams(lineParams);
                dotColumn.addView(line);
            }

            LinearLayout textColumn = new LinearLayout(requireContext());
            textColumn.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textParams.leftMargin = (int) (16 * density);
            if (!isLast) textParams.bottomMargin = (int) (20 * density);
            textColumn.setLayoutParams(textParams);

            StatusStyle style = AdminStatusUi.orderStatusStyle(event.getStatus());
            TextView statusText = new TextView(requireContext());
            statusText.setText(style.getLabel());
            statusText.setTextColor(getResources().getColor(index == 0 ? R.color.pp_text_primary : R.color.pp_text_secondary, null));
            statusText.setTextSize(14f);
            if (index == 0) statusText.setTypeface(statusText.getTypeface(), Typeface.BOLD);
            textColumn.addView(statusText);

            TextView timeText = new TextView(requireContext());
            timeText.setText(dateTimeFormat.format(event.getTimestamp()));
            timeText.setTextColor(getResources().getColor(R.color.pp_disabled, null));
            timeText.setTextSize(11f);
            timeText.setPadding(0, (int) (2 * density), 0, 0);
            textColumn.addView(timeText);

            row.addView(dotColumn);
            row.addView(textColumn);
            getBinding().llTimeline.addView(row);
        }
    }
}
