package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.Order
import com.example.purepawapp.databinding.FragmentAdminOrderDetailBinding
import com.example.purepawapp.databinding.ItemAdminOrderLineBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toVndString
import com.example.purepawapp.util.toast
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class AdminOrderDetailFragment : BaseFragment<FragmentAdminOrderDetailBinding>(FragmentAdminOrderDetailBinding::inflate) {

    private val orderId: String by lazy { requireArguments().getString("orderId").orEmpty() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        loadOrder()
    }

    private fun loadOrder() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.orderRepository.getOrder(orderId)
                .onSuccess { bind(it) }
                .onFailure { toast(it.message ?: "Không thể tải đơn hàng") }
        }
    }

    private fun bind(order: Order) {
        binding.tvOrderCode.text = order.orderCode.ifBlank { "#${order.id.take(8)}" }

        val style = orderStatusStyle(order.status)
        binding.tvStatus.text = style.label
        binding.tvStatus.setTextColor(resources.getColor(style.textColorRes, null))
        binding.tvStatus.backgroundTintList = resources.getColorStateList(style.bgColorRes, null)

        binding.llOrderItems.removeAllViews()
        order.items.forEach { item ->
            val lineBinding = ItemAdminOrderLineBinding.inflate(LayoutInflater.from(requireContext()), binding.llOrderItems, false)
            lineBinding.tvLineName.text = "${item.productName} x${item.quantity}"
            lineBinding.tvLineSubtotal.text = item.subtotal.toVndString()
            binding.llOrderItems.addView(lineBinding.root)
        }

        binding.tvShippingName.text = "${order.shippingAddress.fullName} · ${order.shippingAddress.phone}"
        binding.tvShippingAddress.text = listOf(
            order.shippingAddress.street,
            order.shippingAddress.ward,
            order.shippingAddress.district,
            order.shippingAddress.city
        ).filter { it.isNotBlank() }.joinToString(", ")

        binding.tvPaymentMethod.text = order.paymentMethod.ifBlank { "COD" }
        binding.tvTotal.text = order.totalAmount.toVndString()

        binding.chipGroupStatus.removeAllViews()
        orderStatusFlow.forEach { status ->
            val chip = Chip(requireContext()).apply {
                text = orderStatusStyle(status).label
                isCheckable = true
                isChecked = status == order.status
                setOnClickListener { updateStatus(status) }
            }
            binding.chipGroupStatus.addView(chip)
        }
    }

    private fun updateStatus(status: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.orderRepository.updateOrderStatus(orderId, status)
                .onSuccess {
                    toast("Đã cập nhật trạng thái")
                    loadOrder()
                }
                .onFailure { toast(it.message ?: "Không thể cập nhật trạng thái") }
        }
    }
}
