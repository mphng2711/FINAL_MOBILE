package com.example.purepawapp.ui.spa

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentAppointmentsBinding
import com.example.purepawapp.databinding.ItemAppointmentHistoryBinding
import com.example.purepawapp.ui.common.BaseFragment

class AppointmentsFragment : BaseFragment<FragmentAppointmentsBinding>(FragmentAppointmentsBinding::inflate) {

    private data class HistoryEntry(
        val bookingId: String,
        val date: String,
        val code: String,
        val service: String,
        val pet: String,
        val price: String,
        val iconEmoji: String,
        val iconBgColor: Int,
        val rating: Int
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val entries = listOf(
            HistoryEntry(
                bookingId = "SP230045",
                date = "📅 10/06/2026 · 10:00–12:00",
                code = "SP230045",
                service = "Spa & Grooming",
                pet = "Lucky (Poodle) · Q.1",
                price = "250.000đ",
                iconEmoji = "✂️",
                iconBgColor = R.color.pp_chip_pink_bg,
                rating = 5
            ),
            HistoryEntry(
                bookingId = "MK230031",
                date = "📅 02/06/2026 · 14:00–15:00",
                code = "MK230031",
                service = "Khám sức khỏe định kỳ",
                pet = "Mochi (Mèo Anh) · Q.3",
                price = "150.000đ",
                iconEmoji = "🏥",
                iconBgColor = R.color.pp_chip_green_bg,
                rating = 4
            ),
            HistoryEntry(
                bookingId = "SP230018",
                date = "📅 18/05/2026 · 09:00–10:00",
                code = "SP230018",
                service = "Tắm & Sấy",
                pet = "Lucky (Poodle) · Q.1",
                price = "120.000đ",
                iconEmoji = "💧",
                iconBgColor = R.color.pp_chip_orange_bg,
                rating = 5
            ),
            HistoryEntry(
                bookingId = "MK230009",
                date = "📅 05/05/2026 · 11:00–11:30",
                code = "MK230009",
                service = "Tiêm phòng dại",
                pet = "Lucky (Poodle) · Q.BT",
                price = "80.000đ",
                iconEmoji = "💉",
                iconBgColor = R.color.pp_chip_blue_bg,
                rating = 5
            )
        )

        val rows = listOf(
            binding.itemHistory1,
            binding.itemHistory2,
            binding.itemHistory3,
            binding.itemHistory4
        )

        rows.zip(entries).forEach { (row, entry) -> bind(row, entry) }
    }

    private fun bind(row: ItemAppointmentHistoryBinding, entry: HistoryEntry) {
        row.tvDate.text = entry.date
        row.tvCode.text = entry.code
        row.tvService.text = entry.service
        row.tvPet.text = entry.pet
        row.tvPrice.text = entry.price
        row.tvIcon.text = entry.iconEmoji
        row.tvIcon.backgroundTintList = ColorStateList.valueOf(resources.getColor(entry.iconBgColor, null))
        row.tvStars.text = "⭐".repeat(entry.rating) + "☆".repeat(5 - entry.rating)
        row.root.setOnClickListener {
            findNavController().navigate(
                R.id.action_appointmentsFragment_to_appointmentRatingFragment,
                bundleOf("bookingId" to entry.bookingId)
            )
        }
    }
}
