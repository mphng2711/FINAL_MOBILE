package com.example.purepawapp.ui.profile;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentAboutBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class AboutFragment extends BaseFragment<FragmentAboutBinding> {

    public AboutFragment() {
        super(FragmentAboutBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }
}
