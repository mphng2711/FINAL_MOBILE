package com.example.purepawapp.ui.product

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.databinding.FragmentProductDetailBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toVndString
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>(FragmentProductDetailBinding::inflate) {

    private var quantity = 1
    private var product: Product? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getString("productId")

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnQuantityMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }

        binding.btnQuantityPlus.setOnClickListener {
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }

        binding.btnAddToCart.setOnClickListener {
            val loadedProduct = product
            if (loadedProduct == null) {
                toast("Không thể thêm sản phẩm vào giỏ hàng")
                return@setOnClickListener
            }
            val variant = loadedProduct.defaultVariant
            if (variant == null) {
                toast("Sản phẩm hiện không có sẵn")
                return@setOnClickListener
            }
            ServiceLocator.cartRepository.addToCart(loadedProduct, variant, quantity)
            val itemCount = ServiceLocator.cartRepository.items.value.sumOf { it.quantity }
            toast("Đã thêm vào giỏ hàng ($itemCount sản phẩm)")
        }

        if (productId.isNullOrBlank()) {
            toast("Không thể tải sản phẩm")
            binding.btnAddToCart.isEnabled = false
            return
        }

        binding.btnAddToCart.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.productRepository.getProduct(productId)
                .onSuccess { fetched ->
                    product = fetched
                    binding.tvProductImage.text = fetched.emoji
                    binding.tvProductName.text = fetched.name
                    binding.tvProductSubtitle.text = fetched.shortDescription
                    binding.tvProductPrice.text = fetched.displayPrice.toVndString()
                    binding.tvProductRating.text =
                        "⭐ ${"%.1f".format(fetched.averageRating)} (${fetched.totalReviews} đánh giá)"
                    binding.tvProductDescription.text = fetched.description
                    binding.btnAddToCart.isEnabled = true
                }
                .onFailure {
                    toast(it.message ?: "Không thể tải sản phẩm")
                }
        }
    }
}
