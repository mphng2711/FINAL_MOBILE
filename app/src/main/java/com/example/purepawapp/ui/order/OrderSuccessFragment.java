package com.example.purepawapp.ui.order;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentOrderSuccessBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class OrderSuccessFragment extends BaseFragment<FragmentOrderSuccessBinding> {

    public OrderSuccessFragment() {
        super(FragmentOrderSuccessBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnDone.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }
}
