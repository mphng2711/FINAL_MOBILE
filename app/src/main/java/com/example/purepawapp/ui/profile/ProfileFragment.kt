package com.example.purepawapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentProfileBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        binding.btnOrderHistory.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderHistoryFragment)
        }
        binding.btnContact.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_contactFragment)
        }
        binding.btnPolicy.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_policyFragment)
        }
        binding.btnAbout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }
        binding.btnLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.authRepository.logout()
                ServiceLocator.sessionManager.logout()
                findNavController().navigate(R.id.action_global_to_auth_nav_graph)
            }
        }

        loadProfile()
    }

    override fun onResume() {
        super.onResume()
        loadProfile()
    }

    private fun loadProfile() {
        val uid = ServiceLocator.authRepository.currentUserId ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.profileRepository.getUser(uid).onSuccess { user ->
                binding.tvProfileName.text = user.fullName.ifBlank { "Khách hàng PurePaw" }
                binding.tvProfileEmail.text = user.email
            }

            val orderCount = ServiceLocator.orderRepository.getOrders(uid).getOrDefault(emptyList()).size
            binding.tvStatOrders.text = orderCount.toString()
            binding.tvOrderHistorySubtitle.text = "$orderCount đơn hàng"

            val bookingCount = ServiceLocator.spaRepository.getBookings(uid).getOrDefault(emptyList()).size
            binding.tvStatBookings.text = bookingCount.toString()
        }
    }
}
