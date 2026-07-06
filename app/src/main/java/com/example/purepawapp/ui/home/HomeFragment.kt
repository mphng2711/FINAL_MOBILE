package com.example.purepawapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentHomeBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.ui.product.ProductCardAdapter
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val cartRepository get() = ServiceLocator.cartRepository

    private val adapter = ProductCardAdapter(
        onClick = { product ->
            findNavController().navigate(
                R.id.action_global_to_productDetailFragment,
                bundleOf("productId" to product.id)
            )
        },
        onAddToCart = { product ->
            val variant = product.defaultVariant
            if (variant == null) {
                toast("Sản phẩm hiện không có sẵn")
            } else {
                cartRepository.addToCart(product, variant, 1)
                val itemCount = cartRepository.items.value.sumOf { it.quantity }
                toast("Đã thêm vào giỏ hàng ($itemCount sản phẩm)")
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFeaturedProducts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvFeaturedProducts.adapter = adapter
        binding.rvFeaturedProducts.addItemDecoration(
            SpacingItemDecoration((12 * resources.displayMetrics.density).toInt(), horizontal = true)
        )

        binding.btnShopNow.setOnClickListener { goToProducts() }
        binding.tvSeeAllCategories.setOnClickListener { goToProducts() }
        binding.tvSeeAllProducts.setOnClickListener { goToProducts() }

        binding.cardCategoryFood.setOnClickListener { goToProducts("food") }
        binding.cardCategoryAccessories.setOnClickListener { goToProducts("accessories") }
        binding.cardCategoryHealth.setOnClickListener { goToProducts("health") }
        binding.cardCategorySpa.setOnClickListener { findNavController().navigate(R.id.spa_nav_graph) }

        loadFeaturedProducts()
    }

    override fun onResume() {
        super.onResume()
        loadFeaturedProducts()
    }

    private fun loadFeaturedProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.getProducts()
                .onSuccess { products ->
                    val featured = products.filter { it.isFeatured }.ifEmpty { products }.take(6)
                    adapter.submitList(featured)
                    binding.tvFeaturedEmpty.visibility = if (featured.isEmpty()) View.VISIBLE else View.GONE
                }
                .onFailure {
                    binding.tvFeaturedEmpty.visibility = View.VISIBLE
                }
        }
    }

    private fun goToProducts(categoryId: String? = null) {
        findNavController().navigate(R.id.product_nav_graph, bundleOf("categoryId" to categoryId))
    }
}
