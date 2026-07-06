package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentSpaBookingSuccessBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class SpaBookingSuccessFragment extends BaseFragment<FragmentSpaBookingSuccessBinding> {

    public SpaBookingSuccessFragment() {
        super(FragmentSpaBookingSuccessBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnDone.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }
}
