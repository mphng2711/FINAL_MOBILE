package com.example.purepawapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.MainActivity
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentAdminMoreBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import kotlinx.coroutines.launch

class AdminMoreFragment : BaseFragment<FragmentAdminMoreBinding>(FragmentAdminMoreBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardCategories.setOnClickListener {
            findNavController().navigate(R.id.action_adminMoreFragment_to_adminCategoriesFragment)
        }
        binding.cardPromotions.setOnClickListener {
            findNavController().navigate(R.id.action_adminMoreFragment_to_adminPromotionsFragment)
        }
        binding.cardBlogs.setOnClickListener {
            findNavController().navigate(R.id.action_adminMoreFragment_to_adminBlogsFragment)
        }
        binding.cardBookings.setOnClickListener {
            findNavController().navigate(R.id.action_adminMoreFragment_to_adminBookingsFragment)
        }
        binding.cardUsers.setOnClickListener {
            findNavController().navigate(R.id.action_adminMoreFragment_to_adminUsersFragment)
        }
        binding.cardLogout.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.authRepository.logout()
                ServiceLocator.sessionManager.logout()
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
            }
        }

        val uid = ServiceLocator.authRepository.currentUserId
        if (uid != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.profileRepository.getUser(uid).onSuccess { user ->
                    binding.tvAdminName.text = user.fullName.ifBlank { "Admin PurePaw" }
                    binding.tvAdminEmail.text = user.email
                }
            }
        }
    }
}
