package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminBookingDetailBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class AdminBookingDetailFragment extends BaseFragment<FragmentAdminBookingDetailBinding> {

    private String bookingId;

    public AdminBookingDetailFragment() {
        super(FragmentAdminBookingDetailBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookingId = requireArguments().getString("bookingId", "");
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        loadBooking();
    }

    private void loadBooking() {
        ServiceLocator.getSpaRepository().getAllBookings(new RepoCallback<>() {
            @Override
            public void onSuccess(List<Booking> bookings) {
                Booking booking = null;
                for (Booking b : bookings) {
                    if (b.getId().equals(bookingId)) {
                        booking = b;
                        break;
                    }
                }
                if (booking != null) {
                    bind(booking);
                } else {
                    ViewUtils.toast(AdminBookingDetailFragment.this, "Không tìm thấy lịch hẹn");
                }
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminBookingDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải lịch hẹn");
            }
        });
    }

    private void bind(Booking booking) {
        getBinding().tvServiceName.setText(booking.getServiceName());

        StatusStyle style = AdminStatusUi.bookingStatusStyle(booking.getStatus());
        getBinding().tvStatus.setText(style.getLabel());
        getBinding().tvStatus.setTextColor(getResources().getColor(style.getTextColorRes(), null));
        getBinding().tvStatus.setBackgroundTintList(getResources().getColorStateList(style.getBgColorRes(), null));

        getBinding().tvPetName.setText(booking.getPet().getName());
        getBinding().tvPetInfo.setText(booking.getPet().getSpecies() + " · " + booking.getPet().getBreed() + " · " + booking.getPet().getWeightKg() + "kg");
        String note = booking.getPet().getNote();
        getBinding().tvPetNote.setText(note == null || note.isBlank() ? "Không có ghi chú" : note);

        getBinding().tvDatetime.setText(booking.getBookingDate() + " · " + booking.getTimeSlot());
        getBinding().tvPrice.setText(CurrencyUtils.toVndString(booking.getPrice()));

        getBinding().chipGroupStatus.removeAllViews();
        for (String status : AdminStatusUi.BOOKING_STATUS_FLOW) {
            Chip chip = new Chip(requireContext());
            chip.setText(AdminStatusUi.bookingStatusStyle(status).getLabel());
            chip.setCheckable(true);
            chip.setChecked(status.equals(booking.getStatus()));
            chip.setOnClickListener(v -> updateStatus(status));
            getBinding().chipGroupStatus.addView(chip);
        }
    }

    private void updateStatus(String status) {
        ServiceLocator.getSpaRepository().updateBookingStatus(bookingId, status, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                ViewUtils.toast(AdminBookingDetailFragment.this, "Đã cập nhật trạng thái");
                loadBooking();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminBookingDetailFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể cập nhật trạng thái");
            }
        });
    }
}
