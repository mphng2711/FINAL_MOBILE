package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAppointmentsBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.notification.NotificationHelper;
import com.example.purepawapp.ui.admin.AdminStatusUi;
import com.example.purepawapp.ui.admin.StatusStyle;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.BookingStatus;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentsFragment extends BaseFragment<FragmentAppointmentsBinding> {

    private final AppointmentAdapter adapter = new AppointmentAdapter(
            booking -> {
                Bundle args = new Bundle();
                args.putString("bookingId", booking.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_appointmentsFragment_to_appointmentRatingFragment, args);
            },
            this::confirmCancel
    );

    private final Map<String, String> lastKnownStatuses = new HashMap<>();
    private ListenerRegistration bookingsListener;
    private boolean firstLoad = true;

    public AppointmentsFragment() {
        super(FragmentAppointmentsBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        getBinding().rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvHistory.setAdapter(adapter);

        listenToBookings();
    }

    @Override
    public void onDestroyView() {
        if (bookingsListener != null) bookingsListener.remove();
        bookingsListener = null;
        super.onDestroyView();
    }

    private void listenToBookings() {
        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) return;

        showLoading();
        bookingsListener = ServiceLocator.getSpaRepository().listenToBookings(userId, new RepoCallback<>() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                if (getBinding() == null) return;
                notifyStatusChanges(bookings);
                adapter.submitList(bookings);
                getBinding().tvEmpty.setVisibility(bookings.isEmpty() ? View.VISIBLE : View.GONE);
                getBinding().rvHistory.setVisibility(bookings.isEmpty() ? View.GONE : View.VISIBLE);
                renderStats(bookings);
                if (firstLoad) {
                    hideLoading();
                    firstLoad = false;
                }
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                ViewUtils.toast(AppointmentsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải lịch sử đặt lịch");
            }
        });
    }

    private void notifyStatusChanges(List<Booking> bookings) {
        if (!firstLoad) {
            for (Booking booking : bookings) {
                String previous = lastKnownStatuses.get(booking.getId());
                if (previous != null && !previous.equals(booking.getStatus())) {
                    StatusStyle style = AdminStatusUi.bookingStatusStyle(booking.getStatus());
                    NotificationHelper.showNotification(requireContext(),
                            "Lịch hẹn " + booking.getServiceName() + " cập nhật",
                            "Trạng thái mới: " + style.getLabel(),
                            booking.getId().hashCode());
                }
            }
        }
        lastKnownStatuses.clear();
        for (Booking booking : bookings) lastKnownStatuses.put(booking.getId(), booking.getStatus());
    }

    private void confirmCancel(Booking booking) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Hủy lịch hẹn")
                .setMessage("Bạn có chắc muốn hủy lịch \"" + booking.getServiceName() + "\"?")
                .setNegativeButton("Không", null)
                .setPositiveButton("Hủy lịch", (dialog, which) -> cancelBooking(booking))
                .show();
    }

    private void cancelBooking(Booking booking) {
        showLoading();
        ServiceLocator.getSpaRepository().updateBookingStatus(booking.getId(), BookingStatus.CANCELLED, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                hideLoading();
                ViewUtils.toast(AppointmentsFragment.this, "Đã hủy lịch hẹn");
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                ViewUtils.toast(AppointmentsFragment.this,
                        error.getMessage() != null ? error.getMessage() : "Không thể hủy lịch, vui lòng thử lại");
            }
        });
    }

    private void renderStats(List<Booking> bookings) {
        int count = bookings.size();
        double totalSpent = 0;
        int ratingSum = 0;
        int ratedCount = 0;
        for (Booking booking : bookings) {
            if (BookingStatus.COMPLETED.equals(booking.getStatus())) totalSpent += booking.getPrice();
            if (booking.getRating() > 0) {
                ratingSum += booking.getRating();
                ratedCount++;
            }
        }
        getBinding().tvStatCount.setText(String.valueOf(count));
        getBinding().tvStatSpent.setText(CurrencyUtils.toVndString(totalSpent));
        double avgRating = ratedCount == 0 ? 0.0 : (double) ratingSum / ratedCount;
        getBinding().tvStatRating.setText(String.format("%.1f⭐", avgRating));
    }
}
