package com.example.purepawapp.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.data.session.SessionManager;
import com.example.purepawapp.databinding.FragmentSplashBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.notification.FcmTokenManager;
import com.example.purepawapp.ui.admin.AdminActivity;
import com.example.purepawapp.ui.common.BaseFragment;

public class SplashFragment extends BaseFragment<FragmentSplashBinding> {

    private static final long SPLASH_DELAY_MS = 1200L;

    public SplashFragment() {
        super(FragmentSplashBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SessionManager sessionManager = ServiceLocator.getSessionManager();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isAdded()) return;

            boolean hasSeenOnboarding = sessionManager.hasSeenOnboardingOnce();
            boolean isLoggedIn = sessionManager.isLoggedInOnce();
            boolean isAdmin = isLoggedIn && sessionManager.isAdminOnce();

            if (isLoggedIn) FcmTokenManager.registerTokenForCurrentUser();

            if (isAdmin) {
                startActivity(new Intent(requireContext(), AdminActivity.class));
                requireActivity().finish();
                return;
            }

            int destination;
            if (!hasSeenOnboarding) {
                destination = R.id.action_splashFragment_to_onboardingFragment;
            } else if (isLoggedIn) {
                destination = R.id.action_splashFragment_to_home_nav_graph;
            } else {
                destination = R.id.action_splashFragment_to_auth_nav_graph;
            }
            NavHostFragment.findNavController(this).navigate(destination);
        }, SPLASH_DELAY_MS);
    }
}
