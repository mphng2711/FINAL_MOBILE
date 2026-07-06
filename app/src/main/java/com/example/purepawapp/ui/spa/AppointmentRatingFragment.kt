package com.example.purepawapp.ui.spa

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentAppointmentRatingBinding
import com.example.purepawapp.ui.common.BaseFragment

class AppointmentRatingFragment :
    BaseFragment<FragmentAppointmentRatingBinding>(FragmentAppointmentRatingBinding::inflate) {

    private var rating = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        stars.forEachIndexed { index, star ->
            star.setOnClickListener {
                rating = index + 1
                updateStars(stars)
            }
        }

        binding.btnSubmitRating.setOnClickListener {
            Toast.makeText(requireContext(), "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun updateStars(stars: List<TextView>) {
        stars.forEachIndexed { index, star ->
            val filled = index < rating
            star.text = if (filled) "★" else "☆"
            star.setTextColor(
                resources.getColor(
                    if (filled) R.color.pp_star_amber else R.color.pp_text_secondary,
                    null
                )
            )
        }
        binding.btnSubmitRating.isEnabled = rating > 0
        binding.btnSubmitRating.backgroundTintList = ColorStateList.valueOf(
            resources.getColor(if (rating > 0) R.color.pp_primary else R.color.pp_disabled, null)
        )
        binding.btnSubmitRating.setTextColor(
            resources.getColor(if (rating > 0) R.color.white else R.color.pp_text_secondary, null)
        )
    }
}
