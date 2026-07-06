package com.example.purepawapp.ui.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentCheckoutPaymentBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class CheckoutPaymentFragment extends BaseFragment<FragmentCheckoutPaymentBinding> {

    public CheckoutPaymentFragment() {
        super(FragmentCheckoutPaymentBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnNext.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_checkoutPaymentFragment_to_checkoutReviewFragment));
    }
}
