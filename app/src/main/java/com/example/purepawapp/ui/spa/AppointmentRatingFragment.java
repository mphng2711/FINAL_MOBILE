package com.example.purepawapp.ui.spa;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentAppointmentRatingBinding;
import com.example.purepawapp.ui.common.BaseFragment;

import java.util.List;

public class AppointmentRatingFragment extends BaseFragment<FragmentAppointmentRatingBinding> {

    private int rating = 0;

    public AppointmentRatingFragment() {
        super(FragmentAppointmentRatingBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        List<TextView> stars = List.of(getBinding().star1, getBinding().star2, getBinding().star3, getBinding().star4, getBinding().star5);
        for (int i = 0; i < stars.size(); i++) {
            int index = i;
            stars.get(i).setOnClickListener(v -> {
                rating = index + 1;
                updateStars(stars);
            });
        }

        getBinding().btnSubmitRating.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
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
