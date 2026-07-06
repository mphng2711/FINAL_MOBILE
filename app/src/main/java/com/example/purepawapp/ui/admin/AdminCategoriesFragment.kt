package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.databinding.FragmentAdminCategoriesBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class AdminCategoriesFragment : BaseFragment<FragmentAdminCategoriesBinding>(FragmentAdminCategoriesBinding::inflate) {

    private var allRows: List<CategoryRow> = emptyList()

    private val adapter = AdminCategoryAdapter(
        onToggleActive = { category, isActive ->
            updateCategoryActive(category, isActive)
        },
        onEdit = { category ->
            findNavController().navigate(
                R.id.action_adminCategoriesFragment_to_adminAddCategoryFragment,
                bundleOf(
                    "categoryId" to category.id,
                    "categoryName" to category.name,
                    "categoryIcon" to categoryEmoji(category)
                )
            )
        },
        onDelete = { category ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục \"${category.name}\"?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa") { _, _ -> deleteCategory(category.id) }
                .show()
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
        binding.rvCategories.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        binding.btnAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_adminCategoriesFragment_to_adminAddCategoryFragment)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = applyFilter(s?.toString().orEmpty())
            override fun afterTextChanged(s: Editable?) = Unit
        })

        loadCategories()
    }

    override fun onResume() {
        super.onResume()
        loadCategories()
    }

    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            val categoriesResult = ServiceLocator.productRepository.getCategories()
            val productsResult = ServiceLocator.productRepository.getProducts()
            categoriesResult
                .onSuccess { categories ->
                    val products: List<Product> = productsResult.getOrDefault(emptyList())
                    allRows = categories.sortedBy { it.sortOrder }.map { category ->
                        CategoryRow(category, products.count { it.categoryId == category.id })
                    }
                    applyFilter(binding.etSearch.text?.toString().orEmpty())
                }
                .onFailure { toast(it.message ?: "Không thể tải danh mục") }
        }
    }

    private fun applyFilter(query: String) {
        val filtered = if (query.isBlank()) {
            allRows
        } else {
            allRows.filter { it.category.name.contains(query, ignoreCase = true) }
        }
        adapter.submitList(filtered)
        binding.emptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateCategoryActive(category: Category, isActive: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.updateCategory(category.copy(isActive = isActive))
                .onFailure {
                    toast(it.message ?: "Không thể cập nhật danh mục")
                    loadCategories()
                }
        }
    }

    private fun deleteCategory(categoryId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.deleteCategory(categoryId)
                .onSuccess {
                    toast("Đã xóa danh mục")
                    loadCategories()
                }
                .onFailure { toast(it.message ?: "Không thể xóa danh mục") }
        }
    }
}
