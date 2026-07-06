package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.BlogPost
import com.example.purepawapp.databinding.FragmentAdminAddBlogBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class AdminAddBlogFragment : BaseFragment<FragmentAdminAddBlogBinding>(FragmentAdminAddBlogBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text?.toString().orEmpty().trim()
            val author = binding.etAuthor.text?.toString().orEmpty().trim()
            val content = binding.etContent.text?.toString().orEmpty().trim()

            if (title.isEmpty() || content.isEmpty()) {
                toast("Vui lòng nhập tiêu đề và nội dung")
                return@setOnClickListener
            }

            binding.btnSave.isEnabled = false
            val blog = BlogPost(
                title = title,
                author = author.ifBlank { "Đội ngũ PURE PAW" },
                contentHtml = "<p>${content.replace("\n", "</p><p>")}</p>",
                status = if (binding.switchPublishNow.isChecked) "published" else "draft",
                publishedAt = System.currentTimeMillis()
            )

            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.blogRepository.createBlog(blog)
                    .onSuccess {
                        toast("Đã đăng bài viết")
                        findNavController().popBackStack()
                    }
                    .onFailure {
                        binding.btnSave.isEnabled = true
                        toast(it.message ?: "Không thể đăng bài viết")
                    }
            }
        }
    }
}
