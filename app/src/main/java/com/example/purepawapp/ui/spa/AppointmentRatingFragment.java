package com.example.purepawapp.ui.spa;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAppointmentRatingBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

import java.util.List;

public class AppointmentRatingFragment extends BaseFragment<FragmentAppointmentRatingBinding> {

    private int rating = 0;
    private String bookingId;

    public AppointmentRatingFragment() {
        super(FragmentAppointmentRatingBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookingId = getArguments() != null ? getArguments().getString("bookingId", "") : "";

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        List<TextView> stars = List.of(getBinding().star1, getBinding().star2, getBinding().star3, getBinding().star4, getBinding().star5);
        for (int i = 0; i < stars.size(); i++) {
            int index = i;
            stars.get(i).setOnClickListener(v -> {
                rating = index + 1;
                updateStars(stars);
            });
        }

        getBinding().btnSubmitRating.setOnClickListener(v -> submitRating());
    }

    private void submitRating() {
        if (rating <= 0 || bookingId == null || bookingId.isBlank()) return;
        String review = getBinding().etComment.getText() != null ? getBinding().etComment.getText().toString().trim() : "";

        getBinding().btnSubmitRating.setEnabled(false);
        showLoading();
        ServiceLocator.getSpaRepository().submitRating(bookingId, rating, review, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                hideLoading();
                Toast.makeText(requireContext(), "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(AppointmentRatingFragment.this).popBackStack();
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                if (getBinding() != null) getBinding().btnSubmitRating.setEnabled(true);
                ViewUtils.toast(AppointmentRatingFragment.this,
                        error.getMessage() != null ? error.getMessage() : "Không thể gửi đánh giá, vui lòng thử lại");
            }
        });
    }

    private void updateStars(List<TextView> stars) {
        for (int i = 0; i < stars.size(); i++) {
            boolean filled = i < rating;
            TextView star = stars.get(i);
            star.setText(filled ? "★" : "☆");
            star.setTextColor(getResources().getColor(filled ? R.color.pp_star_amber : R.color.pp_text_secondary, null));
        }
        getBinding().btnSubmitRating.setEnabled(rating > 0);
        getBinding().btnSubmitRating.setBackgroundTintList(ColorStateList.valueOf(
                getResources().getColor(rating > 0 ? R.color.pp_primary : R.color.pp_disabled, null)));
        getBinding().btnSubmitRating.setTextColor(
                getResources().getColor(rating > 0 ? R.color.white : R.color.pp_text_secondary, null));
    }
}
