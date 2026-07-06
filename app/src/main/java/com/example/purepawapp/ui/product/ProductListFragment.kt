package com.example.purepawapp.ui.product

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentProductListBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class ProductListFragment : BaseFragment<FragmentProductListBinding>(FragmentProductListBinding::inflate) {

    private val adapter = ProductRowAdapter(
        onClick = { product ->
            findNavController().navigate(
                R.id.action_global_to_productDetailFragment,
                bundleOf("productId" to product.id)
            )
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter
        binding.rvProducts.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        val categoryId = arguments?.getString("categoryId")

        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.getProducts(categoryId)
                .onSuccess { products ->
                    adapter.submitList(products)
                    binding.emptyState.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
                }
                .onFailure {
                    toast(it.message ?: "Không thể tải danh sách sản phẩm")
                }
        }
    }
}
