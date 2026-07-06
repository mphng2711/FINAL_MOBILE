package com.example.purepawapp.ui.onboarding;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentOnboardingBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;

import java.util.List;

public class OnboardingFragment extends BaseFragment<FragmentOnboardingBinding> {

    private final List<OnboardingPage> pages = List.of(
            new OnboardingPage("Everything Your Pet Needs",
                    "Shop food, toys, and accessories for your furry friend in one place."),
            new OnboardingPage("Book Spa Care Instantly",
                    "Reserve bathing, grooming, and massage sessions in just a few taps."),
            new OnboardingPage("Track Every Step",
                    "Follow your orders and appointments in real time, right from your phone.")
    );

    public OnboardingFragment() {
        super(FragmentOnboardingBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().viewPagerOnboarding.setAdapter(new OnboardingPagerAdapter(pages));

        getBinding().btnGetStarted.setOnClickListener(v -> {
            ServiceLocator.getSessionManager().markOnboardingSeen();
            NavHostFragment.findNavController(this).navigate(R.id.action_onboardingFragment_to_auth_nav_graph);
        });
    }
}
