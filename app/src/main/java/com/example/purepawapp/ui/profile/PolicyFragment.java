package com.example.purepawapp.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentPolicyBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class PolicyFragment extends BaseFragment<FragmentPolicyBinding> {

    public PolicyFragment() {
        super(FragmentPolicyBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().rowPolicy1.setOnClickListener(v -> toggle(getBinding().contentPolicy1, getBinding().tvChevron1));
        getBinding().rowPolicy2.setOnClickListener(v -> toggle(getBinding().contentPolicy2, getBinding().tvChevron2));
    }

    private void toggle(View content, TextView chevron) {
        boolean expanded = content.getVisibility() == View.VISIBLE;
        content.setVisibility(expanded ? View.GONE : View.VISIBLE);
        chevron.setText(expanded ? "▼" : "▲");
    }
}
