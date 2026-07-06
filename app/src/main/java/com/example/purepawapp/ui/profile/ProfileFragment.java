package com.example.purepawapp.ui.profile;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentProfileBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    public ProfileFragment() {
        super(FragmentProfileBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnEditProfile.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_editProfileFragment));
        getBinding().btnOrderHistory.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_orderHistoryFragment));
        getBinding().btnContact.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_contactFragment));
        getBinding().btnPolicy.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_policyFragment));
        getBinding().btnAbout.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_aboutFragment));
        getBinding().btnLogout.setOnClickListener(v -> {
            ServiceLocator.getAuthRepository().logout();
            ServiceLocator.getSessionManager().logout();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_to_auth_nav_graph);
        });

        loadProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }

    private void loadProfile() {
        String uid = ServiceLocator.getAuthRepository().getCurrentUserId();
        if (uid == null) return;
        showLoading();
        AtomicInteger pending = new AtomicInteger(3);
        Runnable done = () -> {
            if (pending.decrementAndGet() == 0) hideLoading();
        };

        ServiceLocator.getProfileRepository().getUser(uid, new RepoCallback<>() {
            @Override
            public void onSuccess(User user) {
                String name = user.getFullName();
                getBinding().tvProfileName.setText(name == null || name.isBlank() ? "Khách hàng PurePaw" : name);
                getBinding().tvProfileEmail.setText(user.getEmail());
                done.run();
            }

            @Override
            public void onError(Exception error) {
                done.run();
            }
        });

        ServiceLocator.getOrderRepository().getOrders(uid, new RepoCallback<>() {
            @Override
            public void onSuccess(List<com.example.purepawapp.data.model.Order> orders) {
                getBinding().tvStatOrders.setText(String.valueOf(orders.size()));
                getBinding().tvOrderHistorySubtitle.setText(orders.size() + " đơn hàng");
                done.run();
            }

            @Override
            public void onError(Exception error) {
                getBinding().tvStatOrders.setText("0");
                getBinding().tvOrderHistorySubtitle.setText("0 đơn hàng");
                done.run();
            }
        });

        ServiceLocator.getSpaRepository().getBookings(uid, new RepoCallback<>() {
            @Override
            public void onSuccess(List<com.example.purepawapp.data.model.Booking> bookings) {
                getBinding().tvStatBookings.setText(String.valueOf(bookings.size()));
                done.run();
            }

            @Override
            public void onError(Exception error) {
                getBinding().tvStatBookings.setText("0");
                done.run();
            }
        });
    }
}
