package com.example.purepawapp.ui.blog;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.databinding.FragmentBlogDetailBinding;
import com.example.purepawapp.ui.common.BaseFragment;

public class BlogDetailFragment extends BaseFragment<FragmentBlogDetailBinding> {

    public BlogDetailFragment() {
        super(FragmentBlogDetailBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }
}
