package com.example.purepawapp.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.MainActivity;
import com.example.purepawapp.R;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminMoreBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;

public class AdminMoreFragment extends BaseFragment<FragmentAdminMoreBinding> {

    public AdminMoreFragment() {
        super(FragmentAdminMoreBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().cardCategories.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminMoreFragment_to_adminCategoriesFragment));
        getBinding().cardPromotions.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminMoreFragment_to_adminPromotionsFragment));
        getBinding().cardBlogs.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminMoreFragment_to_adminBlogsFragment));
        getBinding().cardBookings.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminMoreFragment_to_adminBookingsFragment));
        getBinding().cardUsers.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminMoreFragment_to_adminUsersFragment));
        getBinding().cardLogout.setOnClickListener(v -> {
            ServiceLocator.getAuthRepository().logout();
            ServiceLocator.getSessionManager().logout();
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        String uid = ServiceLocator.getAuthRepository().getCurrentUserId();
        if (uid != null) {
            ServiceLocator.getProfileRepository().getUser(uid, new RepoCallback<>() {
                @Override
                public void onSuccess(User user) {
                    String name = user.getFullName();
                    getBinding().tvAdminName.setText(name == null || name.isBlank() ? "Admin PurePaw" : name);
                    getBinding().tvAdminEmail.setText(user.getEmail());
                }

                @Override
                public void onError(Exception error) {
                }
            });
        }
    }
}
