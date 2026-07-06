package com.example.purepawapp.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentCheckoutPaymentBinding
import com.example.purepawapp.ui.common.BaseFragment

class CheckoutPaymentFragment :
    BaseFragment<FragmentCheckoutPaymentBinding>(FragmentCheckoutPaymentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutPaymentFragment_to_checkoutReviewFragment)
        }
    }
}
