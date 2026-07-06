package com.example.purepawapp.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentContactBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class ContactFragment extends BaseFragment<FragmentContactBinding> {

    public ContactFragment() {
        super(FragmentContactBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSend.setOnClickListener(v -> {
            getBinding().etName.getText().clear();
            getBinding().etEmail.getText().clear();
            getBinding().etMessage.getText().clear();
            Toast.makeText(requireContext(), "Đã gửi tin nhắn!", Toast.LENGTH_SHORT).show();
        });
    }
}
