package com.example.purepawapp.ui.order

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.databinding.FragmentOrderSuccessBinding
import com.example.purepawapp.ui.common.BaseFragment

class OrderSuccessFragment : BaseFragment<FragmentOrderSuccessBinding>(FragmentOrderSuccessBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDone.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
