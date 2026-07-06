package com.example.purepawapp.ui.order

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Order
import com.example.purepawapp.data.model.OrderStatusEvent
import com.example.purepawapp.databinding.FragmentOrderDetailBinding
import com.example.purepawapp.databinding.ItemAdminOrderLineBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.admin.orderStatusStyle
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toVndString
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(FragmentOrderDetailBinding::inflate) {

    private val orderId: String by lazy { arguments?.getString("orderId").orEmpty() }
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
    private val dateTimeFormat = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale("vi", "VN"))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        loadOrder()
    }

    override fun onResume() {
        super.onResume()
        loadOrder()
    }

    private fun loadOrder() {
        if (orderId.isBlank()) return
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.orderRepository.getOrder(orderId)
                .onSuccess { bind(it) }
                .onFailure { toast(it.message ?: "Không thể tải đơn hàng") }
        }
    }

    private fun bind(order: Order) {
        val style = orderStatusStyle(order.status)
        binding.tvHeroTitle.text = style.label
        binding.tvHeroSubtitle.text = "Ngày đặt: ${dateFormat.format(order.createdAt)}"
        binding.tvOrderCode.text = "Mã đơn: ${order.orderCode.ifBlank { "#${order.id.take(8)}" }}"

        renderTimeline(order)

        binding.llOrderItems.removeAllViews()
        order.items.forEach { item ->
            val lineBinding = ItemAdminOrderLineBinding.inflate(LayoutInflater.from(requireContext()), binding.llOrderItems, false)
            lineBinding.tvLineName.text = "${item.productName} x${item.quantity}"
            lineBinding.tvLineSubtotal.text = item.subtotal.toVndString()
            binding.llOrderItems.addView(lineBinding.root)
        }

        binding.tvShippingName.text = "${order.shippingAddress.fullName} | ${order.shippingAddress.phone}"
        binding.tvShippingAddress.text = listOf(
            order.shippingAddress.street,
            order.shippingAddress.ward,
            order.shippingAddress.district,
            order.shippingAddress.city
        ).filter { it.isNotBlank() }.joinToString(", ")

        binding.tvPaymentMethod.text = when (order.paymentMethod.lowercase()) {
            "cod" -> "Thanh toán khi nhận hàng (COD)"
            "bank", "banking" -> "Chuyển khoản ngân hàng"
            else -> order.paymentMethod.ifBlank { "Thanh toán khi nhận hàng (COD)" }
        }

        binding.tvSubtotal.text = order.subtotal.toVndString()
        binding.tvShippingFee.text = order.shippingFee.toVndString()
        if (order.discountAmount > 0) {
            binding.rowDiscount.visibility = View.VISIBLE
            binding.tvDiscount.text = "-${order.discountAmount.toVndString()}"
        } else {
            binding.rowDiscount.visibility = View.GONE
        }
        binding.tvTotalAmount.text = order.totalAmount.toVndString()
    }

    private fun renderTimeline(order: Order) {
        binding.llTimeline.removeAllViews()
        val density = resources.displayMetrics.density
        val history = order.statusHistory.ifEmpty {
            listOf(OrderStatusEvent(status = order.status, timestamp = order.createdAt))
        }.sortedByDescending { it.timestamp }

        history.forEachIndexed { index, event ->
            val isLast = index == history.lastIndex
            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val dotColumn = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    (12 * density).toInt(),
                    if (isLast) LinearLayout.LayoutParams.WRAP_CONTENT else LinearLayout.LayoutParams.MATCH_PARENT
                )
            }
            val dot = View(requireContext()).apply {
                background = resources.getDrawable(R.drawable.shape_circle, null)
                backgroundTintList = resources.getColorStateList(
                    if (index == 0) R.color.pp_primary else R.color.pp_timeline_inactive,
                    null
                )
                layoutParams = LinearLayout.LayoutParams((12 * density).toInt(), (12 * density).toInt())
            }
            dotColumn.addView(dot)
            if (!isLast) {
                val line = View(requireContext()).apply {
                    setBackgroundColor(resources.getColor(R.color.pp_timeline_inactive, null))
                    layoutParams = LinearLayout.LayoutParams((2 * density).toInt(), 0, 1f)
                }
                dotColumn.addView(line)
            }

            val textColumn = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    marginStart = (16 * density).toInt()
                    if (!isLast) bottomMargin = (20 * density).toInt()
                }
            }
            val style = orderStatusStyle(event.status)
            textColumn.addView(TextView(requireContext()).apply {
                text = style.label
                setTextColor(resources.getColor(if (index == 0) R.color.pp_text_primary else R.color.pp_text_secondary, null))
                textSize = 14f
                if (index == 0) setTypeface(typeface, android.graphics.Typeface.BOLD)
            })
            textColumn.addView(TextView(requireContext()).apply {
                text = dateTimeFormat.format(event.timestamp)
                setTextColor(resources.getColor(R.color.pp_disabled, null))
                textSize = 11f
                setPadding(0, (2 * density).toInt(), 0, 0)
            })

            row.addView(dotColumn)
            row.addView(textColumn)
            binding.llTimeline.addView(row)
        }
    }
}
