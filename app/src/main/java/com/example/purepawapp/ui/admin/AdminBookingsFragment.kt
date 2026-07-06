package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentAdminBookingsBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class AdminBookingsFragment : BaseFragment<FragmentAdminBookingsBinding>(FragmentAdminBookingsBinding::inflate) {

    private val adapter = AdminBookingAdapter(
        onClick = { booking ->
            findNavController().navigate(
                R.id.action_adminBookingsFragment_to_adminBookingDetailFragment,
                bundleOf("bookingId" to booking.id)
            )
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookings.adapter = adapter
        binding.rvBookings.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))
        loadBookings()
    }

    override fun onResume() {
        super.onResume()
        loadBookings()
    }

    private fun loadBookings() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.spaRepository.getAllBookings()
                .onSuccess { bookings ->
                    adapter.submitList(bookings)
                    binding.emptyState.visibility = if (bookings.isEmpty()) View.VISIBLE else View.GONE
                }
                .onFailure { toast(it.message ?: "Không thể tải danh sách lịch hẹn") }
        }
    }
}
