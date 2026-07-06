package com.example.purepawapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentSplashBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.admin.AdminActivity
import com.example.purepawapp.ui.common.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_DELAY_MS = 1200L

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = ServiceLocator.sessionManager

        viewLifecycleOwner.lifecycleScope.launch {
            delay(SPLASH_DELAY_MS)
            if (!isAdded) return@launch

            val hasSeenOnboarding = sessionManager.hasSeenOnboardingOnce()
            val isLoggedIn = sessionManager.isLoggedInOnce()
            val isAdmin = isLoggedIn && sessionManager.isAdminOnce()

            if (isAdmin) {
                startActivity(Intent(requireContext(), AdminActivity::class.java))
                requireActivity().finish()
                return@launch
            }

            val destination = when {
                !hasSeenOnboarding -> R.id.action_splashFragment_to_onboardingFragment
                isLoggedIn -> R.id.action_splashFragment_to_home_nav_graph
                else -> R.id.action_splashFragment_to_auth_nav_graph
            }
            findNavController().navigate(destination)
        }
    }
}
