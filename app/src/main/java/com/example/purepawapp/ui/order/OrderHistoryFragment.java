package com.example.purepawapp.ui.order;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentOrderHistoryBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;

import java.util.List;

public class OrderHistoryFragment extends BaseFragment<FragmentOrderHistoryBinding> {

    private final OrderHistoryAdapter adapter = new OrderHistoryAdapter(order -> {
        Bundle args = new Bundle();
        args.putString("orderId", order.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_orderHistoryFragment_to_orderDetailFragment, args);
    });

    public OrderHistoryFragment() {
        super(FragmentOrderHistoryBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvOrders.setAdapter(adapter);
        getBinding().rvOrders.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        loadOrders();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        String uid = ServiceLocator.getAuthRepository().getCurrentUserId();
        if (uid == null) return;
        showLoading();
        ServiceLocator.getOrderRepository().getOrders(uid, new RepoCallback<>() {
            @Override
            public void onSuccess(List<Order> orders) {
                adapter.submitList(orders);
                getBinding().emptyState.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
                hideLoading();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(OrderHistoryFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải lịch sử đơn hàng");
                hideLoading();
            }
        });
    }
}
