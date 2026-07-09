package com.example.purepawapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentLoginBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.notification.FcmTokenManager;
import com.example.purepawapp.ui.admin.AdminActivity;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    public LoginFragment() {
        super(FragmentLoginBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnGoSignup.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_signupFragment));

        getBinding().btnForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

        getBinding().btnLogin.setOnClickListener(v -> {
            String email = getText(getBinding().tilEmail).trim();
            String password = getText(getBinding().tilPassword);

            if (email.isEmpty() || password.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập đầy đủ email và mật khẩu");
                return;
            }

            getBinding().btnLogin.setEnabled(false);
            showLoading();
            ServiceLocator.getAuthRepository().login(email, password, new com.example.purepawapp.data.repository.RepoCallback<>() {
                @Override
                public void onSuccess(String uid) {
                    ServiceLocator.getProfileRepository().getUser(uid, new com.example.purepawapp.data.repository.RepoCallback<>() {
                        @Override
                        public void onSuccess(com.example.purepawapp.data.model.User user) {
                            finishLogin(uid, user.getRole());
                        }

                        @Override
                        public void onError(Exception error) {
                            finishLogin(uid, "");
                        }
                    });
                }

                @Override
                public void onError(Exception error) {
                    hideLoading();
                    getBinding().btnLogin.setEnabled(true);
                    ViewUtils.toast(LoginFragment.this, loginErrorMessage(error));
                }
            });
        });
    }

    private void showForgotPasswordDialog() {
        EditText input = new EditText(requireContext());
        input.setHint("Email đã đăng ký");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setText(getText(getBinding().tilEmail).trim());
        int padding = (int) (20 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding / 2, padding, 0);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Quên mật khẩu")
                .setMessage("Nhập email của bạn, chúng tôi sẽ gửi liên kết đặt lại mật khẩu.")
                .setView(input)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    String email = input.getText() != null ? input.getText().toString().trim() : "";
                    if (email.isEmpty()) {
                        ViewUtils.toast(this, "Vui lòng nhập email");
                        return;
                    }
                    sendPasswordReset(email);
                })
                .show();
    }

    private void sendPasswordReset(String email) {
        showLoading();
        ServiceLocator.getAuthRepository().sendPasswordReset(email, new com.example.purepawapp.data.repository.RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                hideLoading();
                ViewUtils.toast(LoginFragment.this, "Đã gửi email đặt lại mật khẩu, vui lòng kiểm tra hộp thư");
            }

            @Override
            public void onError(Exception error) {
                hideLoading();
                ViewUtils.toast(LoginFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể gửi email, vui lòng thử lại");
            }
        });
    }

    private void finishLogin(String uid, String role) {
        ServiceLocator.getSessionManager().onLoginSuccess(uid, role, true);
        FcmTokenManager.registerTokenForCurrentUser();
        hideLoading();
        if ("admin".equals(role)) {
            startActivity(new Intent(requireContext(), AdminActivity.class));
            requireActivity().finish();
        } else {
            NavHostFragment.findNavController(this).navigate(R.id.action_global_to_home_nav_graph);
        }
    }

    private String getText(com.google.android.material.textfield.TextInputLayout layout) {
        return layout.getEditText() != null && layout.getEditText().getText() != null
                ? layout.getEditText().getText().toString()
                : "";
    }

    private String loginErrorMessage(Exception error) {
        if (error instanceof FirebaseAuthInvalidCredentialsException) {
            return "Email hoặc mật khẩu không đúng";
        } else if (error instanceof FirebaseAuthInvalidUserException) {
            return "Tài khoản không tồn tại";
        } else {
            return "Đăng nhập thất bại: " + error.getMessage();
        }
    }
}
