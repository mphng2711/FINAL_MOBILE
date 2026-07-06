package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.databinding.FragmentAdminProductsBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class AdminProductsFragment : BaseFragment<FragmentAdminProductsBinding>(FragmentAdminProductsBinding::inflate) {

    private var allProducts: List<Product> = emptyList()

    private val adapter = AdminProductAdapter(
        onDelete = { product ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa \"${product.name}\"?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa") { _, _ -> deleteProduct(product.id) }
                .show()
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        binding.btnAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_adminProductsFragment_to_adminAddProductFragment)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = applyFilter(s?.toString().orEmpty())
            override fun afterTextChanged(s: Editable?) = Unit
        })

        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.getProducts()
                .onSuccess {
                    allProducts = it
                    applyFilter(binding.etSearch.text?.toString().orEmpty())
                }
                .onFailure { toast(it.message ?: "Không thể tải danh sách sản phẩm") }
        }
    }

    private fun applyFilter(query: String) {
        val filtered = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.submitList(filtered)
        binding.emptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun deleteProduct(productId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.deleteProduct(productId)
                .onSuccess {
                    toast("Đã xóa sản phẩm")
                    loadProducts()
                }
                .onFailure { toast(it.message ?: "Không thể xóa sản phẩm") }
        }
    }
}
