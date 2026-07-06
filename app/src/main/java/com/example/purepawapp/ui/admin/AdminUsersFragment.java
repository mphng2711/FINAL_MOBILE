package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminUsersBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUsersFragment extends BaseFragment<FragmentAdminUsersBinding> {

    private static final long SEVEN_DAYS_MS = 7L * 24 * 60 * 60 * 1000;

    private List<User> allUsers = List.of();
    private Map<String, Integer> orderCounts = new HashMap<>();

    private final AdminUserAdapter adapter = new AdminUserAdapter();

    public AdminUsersFragment() {
        super(FragmentAdminUsersBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        getBinding().rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvUsers.setAdapter(adapter);
        getBinding().rvUsers.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

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

        loadUsers();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        ServiceLocator.getOrderRepository().getAllOrders(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Order> orders) {
                Map<String, Integer> counts = new HashMap<>();
                for (Order order : orders) counts.merge(order.getUserId(), 1, Integer::sum);
                orderCounts = counts;
                adapter.updateOrderCounts(orderCounts);
                fetchUsers();
            }

            @Override
            public void onError(Exception error) {
                fetchUsers();
            }
        });
    }

    private void fetchUsers() {
        ServiceLocator.getProfileRepository().getAllUsers(new RepoCallback<>() {
            @Override
            public void onSuccess(List<User> users) {
                allUsers = users;
                long now = System.currentTimeMillis();
                int newCount = 0;
                int activeCount = 0;
                for (User u : users) {
                    if (now - u.getCreatedAt() <= SEVEN_DAYS_MS) newCount++;
                    Integer count = orderCounts.get(u.getUid());
                    if (count != null && count > 0) activeCount++;
                }
                getBinding().tvTotalCount.setText(String.valueOf(users.size()));
                getBinding().tvNewCount.setText(String.valueOf(newCount));
                getBinding().tvActiveCount.setText(String.valueOf(activeCount));
                applyFilter();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminUsersFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách người dùng");
            }
        });
    }

    private void applyFilter() {
        String query = getBinding().etSearch.getText() != null ? getBinding().etSearch.getText().toString().trim() : "";
        List<User> filtered = new ArrayList<>();
        boolean customersOnly = getBinding().chipGroupFilter.getCheckedChipId() == getBinding().chipCustomers.getId();
        String lowerQuery = query.toLowerCase();
        for (User user : allUsers) {
            if (customersOnly && "admin".equals(user.getRole())) continue;
            if (!query.isEmpty() && !user.getFullName().toLowerCase().contains(lowerQuery) && !user.getEmail().toLowerCase().contains(lowerQuery)) {
                continue;
            }
            filtered.add(user);
        }
        adapter.submitList(filtered);
    }
}
