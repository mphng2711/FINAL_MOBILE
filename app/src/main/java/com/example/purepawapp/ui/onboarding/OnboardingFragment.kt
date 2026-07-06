package com.example.purepawapp.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentOnboardingBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import kotlinx.coroutines.launch

class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    private val pages = listOf(
        OnboardingPage(
            title = "Everything Your Pet Needs",
            description = "Shop food, toys, and accessories for your furry friend in one place."
        ),
        OnboardingPage(
            title = "Book Spa Care Instantly",
            description = "Reserve bathing, grooming, and massage sessions in just a few taps."
        ),
        OnboardingPage(
            title = "Track Every Step",
            description = "Follow your orders and appointments in real time, right from your phone."
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerOnboarding.adapter = OnboardingPagerAdapter(pages)

        binding.btnGetStarted.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.sessionManager.markOnboardingSeen()
                findNavController().navigate(R.id.action_onboardingFragment_to_auth_nav_graph)
            }
        }
    }
}
