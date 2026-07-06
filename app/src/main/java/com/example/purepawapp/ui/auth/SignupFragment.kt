package com.example.purepawapp.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentSignupBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class SignupFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignup.setOnClickListener {
            val name = binding.tilName.editText?.text?.toString().orEmpty().trim()
            val email = binding.tilEmail.editText?.text?.toString().orEmpty().trim()
            val phone = binding.tilPhone.editText?.text?.toString().orEmpty().trim()
            val password = binding.tilPassword.editText?.text?.toString().orEmpty()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                toast("Vui lòng nhập đầy đủ thông tin")
                return@setOnClickListener
            }

            val role = if (binding.chipGroupRole.checkedChipId == binding.chipRoleAdmin.id) "admin" else "user"

            binding.btnSignup.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.authRepository.signUp(name, email, password, phone, role)
                    .onSuccess {
                        ServiceLocator.authRepository.logout()
                        toast("Đăng ký thành công! Vui lòng đăng nhập.")
                        findNavController().popBackStack()
                    }
                    .onFailure {
                        binding.btnSignup.isEnabled = true
                        toast("Đăng ký thất bại: ${it.message}")
                    }
            }
        }
    }
}
