package com.example.purepawapp.ui.cart

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentCartBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toVndString
import com.example.purepawapp.util.toast
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

private const val SHIPPING_FEE = 25000.0

class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private val cartRepository get() = ServiceLocator.cartRepository

    private val adapter = CartAdapter(
        onIncrease = { item -> cartRepository.updateQuantity(item.product.id, item.variant.id, item.quantity + 1) },
        onDecrease = { item -> cartRepository.updateQuantity(item.product.id, item.variant.id, item.quantity - 1) },
        onRemove = { item -> cartRepository.removeFromCart(item.product.id, item.variant.id) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = adapter

        binding.btnApplyPromo.setOnClickListener {
            val code = binding.etPromoCode.text?.toString().orEmpty().trim()
            if (code.isEmpty()) {
                toast("Vui lòng nhập mã giảm giá")
                return@setOnClickListener
            }
            viewLifecycleOwner.lifecycleScope.launch {
                cartRepository.applyPromoCode(code)
                    .onSuccess { toast("Áp dụng mã giảm giá thành công") }
                    .onFailure { toast(it.message ?: "Mã giảm giá không hợp lệ") }
            }
        }

        binding.btnCheckout.setOnClickListener {
            if (cartRepository.items.value.isEmpty()) {
                toast("Giỏ hàng của bạn đang trống")
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_cartFragment_to_checkoutAddressFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(cartRepository.items, cartRepository.appliedPromotion) { items, _ -> items }
                    .collect { items ->
                        adapter.submitList(items)

                        val isEmpty = items.isEmpty()
                        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
                        binding.cartContent.visibility = if (isEmpty) View.GONE else View.VISIBLE

                        val subtotal = cartRepository.subtotal()
                        val discount = cartRepository.discount()
                        val shipping = if (isEmpty) 0.0 else SHIPPING_FEE
                        val total = cartRepository.total() + shipping

                        binding.tvSubtotal.text = subtotal.toVndString()
                        binding.tvShipping.text = shipping.toVndString()
                        binding.tvDiscount.text = "-${discount.toVndString()}"
                        binding.tvTotal.text = total.toVndString()
                        binding.tvTotalBottom.text = total.toVndString()
                        binding.btnCheckout.isEnabled = !isEmpty
                    }
            }
        }
    }
}
