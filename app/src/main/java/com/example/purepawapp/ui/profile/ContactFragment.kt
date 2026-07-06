package com.example.purepawapp.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.databinding.FragmentContactBinding
import com.example.purepawapp.ui.common.BaseFragment

class ContactFragment : BaseFragment<FragmentContactBinding>(FragmentContactBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSend.setOnClickListener {
            binding.etName.text?.clear()
            binding.etEmail.text?.clear()
            binding.etMessage.text?.clear()
            Toast.makeText(requireContext(), "Đã gửi tin nhắn!", Toast.LENGTH_SHORT).show()
        }
    }
}
