package com.example.purepawapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentLoginBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.admin.AdminActivity
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toast
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text?.toString().orEmpty().trim()
            val password = binding.tilPassword.editText?.text?.toString().orEmpty()

            if (email.isEmpty() || password.isEmpty()) {
                toast("Vui lòng nhập đầy đủ email và mật khẩu")
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.authRepository.login(email, password)
                    .onSuccess { uid ->
                        val role = ServiceLocator.profileRepository.getUser(uid)
                            .getOrNull()?.role.orEmpty()
                        ServiceLocator.sessionManager.onLoginSuccess(uid, role, rememberMe = true)
                        if (role == "admin") {
                            startActivity(Intent(requireContext(), AdminActivity::class.java))
                            requireActivity().finish()
                        } else {
                            findNavController().navigate(R.id.action_global_to_home_nav_graph)
                        }
                    }
                    .onFailure {
                        binding.btnLogin.isEnabled = true
                        toast(loginErrorMessage(it))
                    }
            }
        }
    }

    private fun loginErrorMessage(error: Throwable): String = when (error) {
        is FirebaseAuthInvalidCredentialsException -> "Email hoặc mật khẩu không đúng"
        is FirebaseAuthInvalidUserException -> "Tài khoản không tồn tại"
        else -> "Đăng nhập thất bại: ${error.message}"
    }
}
