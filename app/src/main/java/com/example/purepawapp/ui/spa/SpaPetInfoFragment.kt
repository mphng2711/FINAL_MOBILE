package com.example.purepawapp.ui.spa

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentSpaPetInfoBinding
import com.example.purepawapp.ui.common.BaseFragment

class SpaPetInfoFragment : BaseFragment<FragmentSpaPetInfoBinding>(FragmentSpaPetInfoBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_spaPetInfoFragment_to_spaConfirmFragment)
        }
    }
}
