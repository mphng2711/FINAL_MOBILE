package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminOrdersBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.OrderStatus;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminOrdersFragment extends BaseFragment<FragmentAdminOrdersBinding> {

    private static final Set<String> PENDING_STATUSES = Set.of(
            OrderStatus.PENDING, OrderStatus.CONFIRMED, OrderStatus.PROCESSING, OrderStatus.SHIPPING);

    private List<Order> allOrders = List.of();
    private Map<String, String> userNames = new HashMap<>();

    private final AdminOrderAdapter adapter = new AdminOrderAdapter(order -> {
        Bundle args = new Bundle();
        args.putString("orderId", order.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_adminOrdersFragment_to_adminOrderDetailFragment, args);
    });

    public AdminOrdersFragment() {
        super(FragmentAdminOrdersBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvOrders.setAdapter(adapter);
        getBinding().rvOrders.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        getBinding().etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        getBinding().chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilter());

        loadOrders();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        ServiceLocator.getProfileRepository().getAllUsers(new RepoCallback<>() {
            @Override
            public void onSuccess(List<User> users) {
                Map<String, String> names = new HashMap<>();
                for (User u : users) names.put(u.getUid(), u.getFullName());
                userNames = names;
                fetchOrders();
            }

            @Override
            public void onError(Exception error) {
                fetchOrders();
            }
        });
    }

    private void fetchOrders() {
        ServiceLocator.getOrderRepository().getAllOrders(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Order> orders) {
                allOrders = orders;
                adapter.updateUserNames(userNames);
                applyFilter();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminOrdersFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách đơn hàng");
            }
        });
    }

    private void applyFilter() {
        String query = getBinding().etSearch.getText() != null ? getBinding().etSearch.getText().toString().trim() : "";
        int checkedId = getBinding().chipGroupFilter.getCheckedChipId();

        List<Order> filtered = new ArrayList<>();
        for (Order order : allOrders) {
            if (checkedId == getBinding().chipPending.getId() && !PENDING_STATUSES.contains(order.getStatus())) continue;
            if (checkedId == getBinding().chipCompleted.getId() && !OrderStatus.DELIVERED.equals(order.getStatus())) continue;
            filtered.add(order);
        }

        if (!query.isEmpty()) {
            List<Order> searched = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            for (Order order : filtered) {
                String customerName = userNames.get(order.getUserId());
                if (customerName == null) customerName = order.getShippingAddress().getFullName();
                if (order.getOrderCode().toLowerCase().contains(lowerQuery) || customerName.toLowerCase().contains(lowerQuery)) {
                    searched.add(order);
                }
            }
            filtered = searched;
        }

        adapter.submitList(filtered);
        getBinding().emptyState.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
