package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.data.model.BlogPost
import com.example.purepawapp.databinding.FragmentAdminBlogsBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class AdminBlogsFragment : BaseFragment<FragmentAdminBlogsBinding>(FragmentAdminBlogsBinding::inflate) {

    private var allBlogs: List<BlogPost> = emptyList()

    private val adapter = AdminBlogAdapter(
        onDelete = { blog ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xóa bài viết")
                .setMessage("Bạn có chắc muốn xóa \"${blog.title}\"?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa") { _, _ -> deleteBlog(blog.id) }
                .show()
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvBlogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBlogs.adapter = adapter
        binding.rvBlogs.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        binding.btnAddBlog.setOnClickListener {
            findNavController().navigate(R.id.action_adminBlogsFragment_to_adminAddBlogFragment)
        }
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, _ -> applyFilter() }

        loadBlogs()
    }

    override fun onResume() {
        super.onResume()
        loadBlogs()
    }

    private fun loadBlogs() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.blogRepository.getBlogs()
                .onSuccess {
                    allBlogs = it
                    applyFilter()
                }
                .onFailure { toast(it.message ?: "Không thể tải danh sách blog") }
        }
    }

    private fun applyFilter() {
        val filtered = when (binding.chipGroupFilter.checkedChipId) {
            binding.chipPublished.id -> allBlogs.filter { it.status == "published" }
            binding.chipDraft.id -> allBlogs.filter { it.status == "draft" }
            else -> allBlogs
        }
        adapter.submitList(filtered)
        binding.emptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun deleteBlog(blogId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.blogRepository.deleteBlog(blogId)
                .onSuccess {
                    toast("Đã xóa bài viết")
                    loadBlogs()
                }
                .onFailure { toast(it.message ?: "Không thể xóa bài viết") }
        }
    }
}
