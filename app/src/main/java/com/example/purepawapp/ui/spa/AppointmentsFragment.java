package com.example.purepawapp.ui.spa;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentAppointmentsBinding;
import com.example.purepawapp.databinding.ItemAppointmentHistoryBinding;
import com.example.purepawapp.ui.common.BaseFragment;

import java.util.List;

public class AppointmentsFragment extends BaseFragment<FragmentAppointmentsBinding> {

    private static class HistoryEntry {
        final String bookingId;
        final String date;
        final String code;
        final String service;
        final String pet;
        final String price;
        final String iconEmoji;
        final int iconBgColor;
        final int rating;

        HistoryEntry(String bookingId, String date, String code, String service, String pet, String price, String iconEmoji, int iconBgColor, int rating) {
            this.bookingId = bookingId;
            this.date = date;
            this.code = code;
            this.service = service;
            this.pet = pet;
            this.price = price;
            this.iconEmoji = iconEmoji;
            this.iconBgColor = iconBgColor;
            this.rating = rating;
        }
    }

    public AppointmentsFragment() {
        super(FragmentAppointmentsBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        List<HistoryEntry> entries = List.of(
                new HistoryEntry("SP230045", "📅 10/06/2026 · 10:00–12:00", "SP230045", "Spa & Grooming",
                        "Lucky (Poodle) · Q.1", "250.000đ", "✂️", R.color.pp_chip_pink_bg, 5),
                new HistoryEntry("MK230031", "📅 02/06/2026 · 14:00–15:00", "MK230031", "Khám sức khỏe định kỳ",
                        "Mochi (Mèo Anh) · Q.3", "150.000đ", "🏥", R.color.pp_chip_green_bg, 4),
                new HistoryEntry("SP230018", "📅 18/05/2026 · 09:00–10:00", "SP230018", "Tắm & Sấy",
                        "Lucky (Poodle) · Q.1", "120.000đ", "💧", R.color.pp_chip_orange_bg, 5),
                new HistoryEntry("MK230009", "📅 05/05/2026 · 11:00–11:30", "MK230009", "Tiêm phòng dại",
                        "Lucky (Poodle) · Q.BT", "80.000đ", "💉", R.color.pp_chip_blue_bg, 5)
        );

        List<ItemAppointmentHistoryBinding> rows = List.of(
                getBinding().itemHistory1, getBinding().itemHistory2, getBinding().itemHistory3, getBinding().itemHistory4
        );

        for (int i = 0; i < rows.size(); i++) {
            bind(rows.get(i), entries.get(i));
        }
    }

    private void bind(ItemAppointmentHistoryBinding row, HistoryEntry entry) {
        row.tvDate.setText(entry.date);
        row.tvCode.setText(entry.code);
        row.tvService.setText(entry.service);
        row.tvPet.setText(entry.pet);
        row.tvPrice.setText(entry.price);
        row.tvIcon.setText(entry.iconEmoji);
        row.tvIcon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(entry.iconBgColor, null)));
        row.tvStars.setText("⭐".repeat(entry.rating) + "☆".repeat(5 - entry.rating));
        row.getRoot().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("bookingId", entry.bookingId);
            NavHostFragment.findNavController(this).navigate(R.id.action_appointmentsFragment_to_appointmentRatingFragment, args);
        });
    }
}
