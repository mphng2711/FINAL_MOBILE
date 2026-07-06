package com.example.purepawapp.ui.spa

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.databinding.FragmentSpaBookingSuccessBinding
import com.example.purepawapp.ui.common.BaseFragment

class SpaBookingSuccessFragment :
    BaseFragment<FragmentSpaBookingSuccessBinding>(FragmentSpaBookingSuccessBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDone.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
