package com.example.purepawapp.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.databinding.FragmentEditProfileBinding
import com.example.purepawapp.ui.common.BaseFragment

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Đã lưu thay đổi!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}
