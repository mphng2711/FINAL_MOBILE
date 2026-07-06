package com.example.purepawapp.ui.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentCheckoutAddressBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class CheckoutAddressFragment extends BaseFragment<FragmentCheckoutAddressBinding> {

    public CheckoutAddressFragment() {
        super(FragmentCheckoutAddressBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnNext.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_checkoutAddressFragment_to_checkoutPaymentFragment));
    }
}
