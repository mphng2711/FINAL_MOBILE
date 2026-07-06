package com.example.purepawapp.ui.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentCheckoutReviewBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;

public class CheckoutReviewFragment extends BaseFragment<FragmentCheckoutReviewBinding> {

    public CheckoutReviewFragment() {
        super(FragmentCheckoutReviewBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnPlaceOrder.setOnClickListener(v -> {
            ServiceLocator.getCartRepository().clearCart();
            NavHostFragment.findNavController(this).navigate(R.id.action_checkoutReviewFragment_to_orderSuccessFragment);
        });
    }
}
