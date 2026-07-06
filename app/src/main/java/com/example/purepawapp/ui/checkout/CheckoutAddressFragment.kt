package com.example.purepawapp.ui.checkout

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentCheckoutAddressBinding
import com.example.purepawapp.ui.common.BaseFragment

class CheckoutAddressFragment :
    BaseFragment<FragmentCheckoutAddressBinding>(FragmentCheckoutAddressBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutAddressFragment_to_checkoutPaymentFragment)
        }
    }
}
