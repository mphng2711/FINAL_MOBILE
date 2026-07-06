package com.example.purepawapp.ui.blog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.databinding.FragmentBlogDetailBinding
import com.example.purepawapp.ui.common.BaseFragment

class BlogDetailFragment : BaseFragment<FragmentBlogDetailBinding>(FragmentBlogDetailBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
