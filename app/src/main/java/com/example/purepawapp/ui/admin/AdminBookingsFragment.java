package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminBookingsBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;

import java.util.List;

public class AdminBookingsFragment extends BaseFragment<FragmentAdminBookingsBinding> {

    private final AdminBookingAdapter adapter = new AdminBookingAdapter(booking -> {
        Bundle args = new Bundle();
        args.putString("bookingId", booking.getId());
        NavHostFragment.findNavController(this).navigate(R.id.action_adminBookingsFragment_to_adminBookingDetailFragment, args);
    });

    public AdminBookingsFragment() {
        super(FragmentAdminBookingsBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().rvBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvBookings.setAdapter(adapter);
        getBinding().rvBookings.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));
        loadBookings();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        ServiceLocator.getSpaRepository().getAllBookings(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                adapter.submitList(bookings);
                getBinding().emptyState.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminBookingsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách lịch hẹn");
            }
        });
    }
}
