package com.example.purepawapp.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentCheckoutReviewBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment

class CheckoutReviewFragment :
    BaseFragment<FragmentCheckoutReviewBinding>(FragmentCheckoutReviewBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnPlaceOrder.setOnClickListener {
            ServiceLocator.cartRepository.clearCart()
            findNavController().navigate(R.id.action_checkoutReviewFragment_to_orderSuccessFragment)
        }
    }
}
