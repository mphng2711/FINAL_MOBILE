package com.example.purepawapp.ui.blog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentBlogListBinding
import com.example.purepawapp.ui.common.BaseFragment

class BlogListFragment : BaseFragment<FragmentBlogListBinding>(FragmentBlogListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val cardToBlogId = mapOf(
            binding.cardBlog1 to "blog-1",
            binding.cardBlog2 to "blog-2",
            binding.cardBlog3 to "blog-3",
            binding.cardBlog4 to "blog-4",
            binding.cardBlog5 to "blog-5"
        )

        cardToBlogId.forEach { (card, blogId) ->
            card.setOnClickListener {
                findNavController().navigate(
                    R.id.action_blogListFragment_to_blogDetailFragment,
                    bundleOf("blogId" to blogId)
                )
            }
        }
    }
}
