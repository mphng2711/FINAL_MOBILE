package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.Booking
import com.example.purepawapp.databinding.FragmentAdminBookingDetailBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toVndString
import com.example.purepawapp.util.toast
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class AdminBookingDetailFragment : BaseFragment<FragmentAdminBookingDetailBinding>(FragmentAdminBookingDetailBinding::inflate) {

    private val bookingId: String by lazy { requireArguments().getString("bookingId").orEmpty() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        loadBooking()
    }

    private fun loadBooking() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.spaRepository.getAllBookings()
                .onSuccess { bookings ->
                    val booking = bookings.firstOrNull { it.id == bookingId }
                    if (booking != null) bind(booking) else toast("Không tìm thấy lịch hẹn")
                }
                .onFailure { toast(it.message ?: "Không thể tải lịch hẹn") }
        }
    }

    private fun bind(booking: Booking) {
        binding.tvServiceName.text = booking.serviceName

        val style = bookingStatusStyle(booking.status)
        binding.tvStatus.text = style.label
        binding.tvStatus.setTextColor(resources.getColor(style.textColorRes, null))
        binding.tvStatus.backgroundTintList = resources.getColorStateList(style.bgColorRes, null)

        binding.tvPetName.text = booking.pet.name
        binding.tvPetInfo.text = "${booking.pet.species} · ${booking.pet.breed} · ${booking.pet.weightKg}kg"
        binding.tvPetNote.text = booking.pet.note.ifBlank { "Không có ghi chú" }

        binding.tvDatetime.text = "${booking.bookingDate} · ${booking.timeSlot}"
        binding.tvPrice.text = booking.price.toVndString()

        binding.chipGroupStatus.removeAllViews()
        bookingStatusFlow.forEach { status ->
            val chip = Chip(requireContext()).apply {
                text = bookingStatusStyle(status).label
                isCheckable = true
                isChecked = status == booking.status
                setOnClickListener { updateStatus(status) }
            }
            binding.chipGroupStatus.addView(chip)
        }
    }

    private fun updateStatus(status: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.spaRepository.updateBookingStatus(bookingId, status)
                .onSuccess {
                    toast("Đã cập nhật trạng thái")
                    loadBooking()
                }
                .onFailure { toast(it.message ?: "Không thể cập nhật trạng thái") }
        }
    }
}
