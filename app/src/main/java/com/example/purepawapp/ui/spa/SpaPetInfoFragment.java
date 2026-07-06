package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentSpaPetInfoBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class SpaPetInfoFragment extends BaseFragment<FragmentSpaPetInfoBinding> {

    public SpaPetInfoFragment() {
        super(FragmentSpaPetInfoBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        getBinding().btnNext.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_spaPetInfoFragment_to_spaConfirmFragment));
    }
}
