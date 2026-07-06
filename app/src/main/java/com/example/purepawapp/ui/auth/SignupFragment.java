package com.example.purepawapp.ui.auth;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentSignupBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

public class SignupFragment extends BaseFragment<FragmentSignupBinding> {

    public SignupFragment() {
        super(FragmentSignupBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnGoLogin.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSignup.setOnClickListener(v -> {
            String name = text(getBinding().tilName).trim();
            String email = text(getBinding().tilEmail).trim();
            String phone = text(getBinding().tilPhone).trim();
            String password = text(getBinding().tilPassword);

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            String role = getBinding().chipGroupRole.getCheckedChipId() == getBinding().chipRoleAdmin.getId() ? "admin" : "user";

            getBinding().btnSignup.setEnabled(false);
            showLoading();
            ServiceLocator.getAuthRepository().signUp(name, email, password, phone, role, new com.example.purepawapp.data.repository.RepoCallback<>() {
                @Override
                public void onSuccess(String result) {
                    hideLoading();
                    ServiceLocator.getAuthRepository().logout();
                    ViewUtils.toast(SignupFragment.this, "Đăng ký thành công! Vui lòng đăng nhập.");
                    NavHostFragment.findNavController(SignupFragment.this).popBackStack();
                }

                @Override
                public void onError(Exception error) {
                    hideLoading();
                    getBinding().btnSignup.setEnabled(true);
                    ViewUtils.toast(SignupFragment.this, "Đăng ký thất bại: " + error.getMessage());
                }
            });
        });
    }

    private String text(com.google.android.material.textfield.TextInputLayout layout) {
        return layout.getEditText() != null && layout.getEditText().getText() != null
                ? layout.getEditText().getText().toString()
                : "";
    }
}
