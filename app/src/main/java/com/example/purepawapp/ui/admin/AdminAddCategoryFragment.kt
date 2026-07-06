package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.databinding.FragmentAdminAddCategoryBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class AdminAddCategoryFragment : BaseFragment<FragmentAdminAddCategoryBinding>(FragmentAdminAddCategoryBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = arguments?.getString("categoryId")
        val isEditMode = !categoryId.isNullOrBlank()
        if (isEditMode) {
            binding.tvTitle.text = "Sửa danh mục"
            binding.etName.setText(arguments?.getString("categoryName").orEmpty())
            binding.etIcon.setText(arguments?.getString("categoryIcon").orEmpty())
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty().trim()
            val icon = binding.etIcon.text?.toString().orEmpty().trim()

            if (name.isEmpty()) {
                toast("Vui lòng nhập tên danh mục")
                return@setOnClickListener
            }

            binding.btnSave.isEnabled = false
            val category = Category(
                id = categoryId.orEmpty(),
                name = name,
                slug = name.lowercase().replace(Regex("\\s+"), "-"),
                image = icon,
                isActive = true
            )

            viewLifecycleOwner.lifecycleScope.launch {
                val result = if (isEditMode) {
                    ServiceLocator.productRepository.updateCategory(category)
                } else {
                    ServiceLocator.productRepository.addCategory(category)
                }
                result
                    .onSuccess {
                        toast(if (isEditMode) "Đã cập nhật danh mục" else "Đã thêm danh mục")
                        findNavController().popBackStack()
                    }
                    .onFailure {
                        binding.btnSave.isEnabled = true
                        toast(it.message ?: "Không thể lưu danh mục")
                    }
            }
        }
    }
}
