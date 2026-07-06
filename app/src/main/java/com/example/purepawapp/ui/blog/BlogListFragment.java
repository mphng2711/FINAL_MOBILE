package com.example.purepawapp.ui.blog;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentBlogListBinding;
import com.example.purepawapp.ui.common.BaseFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlogListFragment extends BaseFragment<FragmentBlogListBinding> {

    public BlogListFragment() {
        super(FragmentBlogListBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        Map<MaterialCardView, String> cardToBlogId = new LinkedHashMap<>();
        cardToBlogId.put(getBinding().cardBlog1, "blog-1");
        cardToBlogId.put(getBinding().cardBlog2, "blog-2");
        cardToBlogId.put(getBinding().cardBlog3, "blog-3");
        cardToBlogId.put(getBinding().cardBlog4, "blog-4");
        cardToBlogId.put(getBinding().cardBlog5, "blog-5");

        for (var entry : cardToBlogId.entrySet()) {
            entry.getKey().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("blogId", entry.getValue());
                NavHostFragment.findNavController(this).navigate(R.id.action_blogListFragment_to_blogDetailFragment, args);
            });
        }
    }
}
