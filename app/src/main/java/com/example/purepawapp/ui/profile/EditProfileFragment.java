package com.example.purepawapp.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentEditProfileBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class EditProfileFragment extends BaseFragment<FragmentEditProfileBinding> {

    public EditProfileFragment() {
        super(FragmentEditProfileBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSave.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}
