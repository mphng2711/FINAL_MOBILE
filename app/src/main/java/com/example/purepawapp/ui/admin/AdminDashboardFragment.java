package com.example.purepawapp.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminDashboardBinding;
import com.example.purepawapp.databinding.ItemAdminOrderBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AdminDashboardFragment extends BaseFragment<FragmentAdminDashboardBinding> {

    public AdminDashboardFragment() {
        super(FragmentAdminDashboardBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().tvSeeAllOrders.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.adminOrdersFragment));

        String uid = ServiceLocator.getAuthRepository().getCurrentUserId();
        if (uid != null) {
            ServiceLocator.getProfileRepository().getUser(uid, new RepoCallback<>() {
                @Override
                public void onSuccess(User user) {
                    String name = user.getFullName();
                    getBinding().tvAdminName.setText(name == null || name.isBlank() ? "Admin PurePaw" : name);
                }

                @Override
                public void onError(Exception error) {
                }
            });
        }

        loadDashboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void loadDashboard() {
        AtomicReference<List<Product>> productsRef = new AtomicReference<>(List.of());
        AtomicReference<List<Order>> ordersRef = new AtomicReference<>(List.of());
        AtomicReference<List<User>> usersRef = new AtomicReference<>(List.of());
        AtomicInteger pending = new AtomicInteger(3);

        Runnable maybeFinish = () -> {
            if (pending.decrementAndGet() == 0) {
                List<Product> products = productsRef.get();
                List<Order> orders = ordersRef.get();
                List<User> users = usersRef.get();

                getBinding().tvProductCount.setText(String.valueOf(products.size()));
                getBinding().tvOrderCount.setText(String.valueOf(orders.size()));
                getBinding().tvUserCount.setText(String.valueOf(users.size()));
                double revenue = 0;
                for (Order o : orders) revenue += o.getTotalAmount();
                getBinding().tvRevenue.setText(AdminStatusUi.toCompactVnd(revenue));

                renderChart(orders);
                Map<String, String> userNames = new HashMap<>();
                for (User u : users) userNames.put(u.getUid(), u.getFullName());
                renderRecentOrders(orders, userNames);
            }
        };

        ServiceLocator.getProductRepository().getProducts(null, new RepoCallback<>() {
            @Override
            public void onSuccess(List<Product> result) {
                productsRef.set(result);
                maybeFinish.run();
            }

            @Override
            public void onError(Exception error) {
                maybeFinish.run();
            }
        });

        ServiceLocator.getOrderRepository().getAllOrders(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Order> result) {
                ordersRef.set(result);
                maybeFinish.run();
            }

            @Override
            public void onError(Exception error) {
                maybeFinish.run();
            }
        });

        ServiceLocator.getProfileRepository().getAllUsers(new RepoCallback<>() {
            @Override
            public void onSuccess(List<User> result) {
                usersRef.set(result);
                maybeFinish.run();
            }

            @Override
            public void onError(Exception error) {
                maybeFinish.run();
            }
        });
    }

    private void renderChart(List<Order> orders) {
        getBinding().llChart.removeAllViews();
        Calendar now = Calendar.getInstance();
        List<Calendar> monthKeys = new ArrayList<>();
        for (int offset = 5; offset >= 0; offset--) {
            Calendar cal = (Calendar) now.clone();
            cal.add(Calendar.MONTH, -offset);
            monthKeys.add(cal);
        }

        List<Double> revenueByMonth = new ArrayList<>();
        for (Calendar cal : monthKeys) {
            double sum = 0;
            for (Order order : orders) {
                Calendar orderCal = Calendar.getInstance();
                orderCal.setTimeInMillis(order.getCreatedAt());
                if (orderCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && orderCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                    sum += order.getTotalAmount();
                }
            }
            revenueByMonth.add(sum);
        }

        double maxRevenue = 1.0;
        for (double v : revenueByMonth) if (v > maxRevenue) maxRevenue = v;

        int maxBarHeightDp = 90;
        float density = getResources().getDisplayMetrics().density;

        for (int index = 0; index < monthKeys.size(); index++) {
            Calendar cal = monthKeys.get(index);
            LinearLayout column = new LinearLayout(requireContext());
            column.setOrientation(LinearLayout.VERTICAL);
            column.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            column.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));

            int barHeightPx = Math.max((int) (maxBarHeightDp * density * (revenueByMonth.get(index) / maxRevenue)), (int) (4 * density));
            View bar = new View(requireContext());
            bar.setBackgroundColor(getResources().getColor(R.color.pp_primary, null));
            bar.setLayoutParams(new LinearLayout.LayoutParams((int) (20 * density), barHeightPx));

            TextView label = new TextView(requireContext());
            label.setText("T" + (cal.get(Calendar.MONTH) + 1));
            label.setTextSize(11f);
            label.setTextColor(getResources().getColor(R.color.pp_text_secondary, null));
            label.setGravity(Gravity.CENTER);
            label.setPadding(0, (int) (4 * density), 0, 0);

            column.addView(bar);
            column.addView(label);
            getBinding().llChart.addView(column);
        }

        if (orders.isEmpty()) {
            getBinding().llChart.removeAllViews();
            TextView empty = new TextView(requireContext());
            empty.setText("Chưa có dữ liệu doanh thu");
            empty.setTextColor(Color.parseColor("#8A7B6A"));
            empty.setTextSize(13f);
            empty.setGravity(Gravity.CENTER);
            empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            getBinding().llChart.addView(empty);
        }
    }

    private void renderRecentOrders(List<Order> orders, Map<String, String> userNames) {
        getBinding().llRecentOrders.removeAllViews();
        List<Order> recent = new ArrayList<>(orders);
        recent.sort(Comparator.comparingLong(Order::getCreatedAt).reversed());
        if (recent.size() > 5) recent = recent.subList(0, 5);
        getBinding().tvNoOrders.setVisibility(recent.isEmpty() ? View.VISIBLE : View.GONE);

        for (Order order : recent) {
            ItemAdminOrderBinding rowBinding = ItemAdminOrderBinding.inflate(getLayoutInflater(), getBinding().llRecentOrders, false);
            String customerName = userNames.get(order.getUserId());
            if (customerName == null || customerName.isBlank()) {
                customerName = order.getShippingAddress().getFullName();
                if (customerName == null || customerName.isBlank()) customerName = "Khách hàng";
            }
            rowBinding.tvCustomerName.setText(customerName);
            String orderCode = order.getOrderCode();
            rowBinding.tvOrderCode.setText(orderCode != null && !orderCode.isBlank() ? orderCode : "#" + order.getId().substring(0, Math.min(8, order.getId().length())));
            int itemCount = 0;
            for (var item : order.getItems()) itemCount += item.getQuantity();
            rowBinding.tvItemCount.setText(itemCount + " sản phẩm");
            rowBinding.tvTotal.setText(CurrencyUtils.toVndString(order.getTotalAmount()));
            StatusStyle style = AdminStatusUi.orderStatusStyle(order.getStatus());
            rowBinding.tvStatus.setText(style.getLabel());
            rowBinding.tvStatus.setTextColor(getResources().getColor(style.getTextColorRes(), null));
            rowBinding.tvStatus.setBackgroundTintList(getResources().getColorStateList(style.getBgColorRes(), null));
            rowBinding.getRoot().setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.adminOrdersFragment));
            if (rowBinding.getRoot().getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rowBinding.getRoot().getLayoutParams();
                params.bottomMargin = (int) (8 * getResources().getDisplayMetrics().density);
            }
            getBinding().llRecentOrders.addView(rowBinding.getRoot());
        }
    }
}
