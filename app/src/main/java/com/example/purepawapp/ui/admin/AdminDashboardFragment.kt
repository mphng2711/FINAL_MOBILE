package com.example.purepawapp.ui.admin

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Order
import com.example.purepawapp.databinding.FragmentAdminDashboardBinding
import com.example.purepawapp.databinding.ItemAdminOrderBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.toVndString
import kotlinx.coroutines.launch
import java.util.Calendar

class AdminDashboardFragment : BaseFragment<FragmentAdminDashboardBinding>(FragmentAdminDashboardBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSeeAllOrders.setOnClickListener {
            findNavController().navigate(R.id.adminOrdersFragment)
        }

        val uid = ServiceLocator.authRepository.currentUserId
        if (uid != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.profileRepository.getUser(uid).onSuccess { user ->
                    binding.tvAdminName.text = user.fullName.ifBlank { "Admin PurePaw" }
                }
            }
        }

        loadDashboard()
    }

    override fun onResume() {
        super.onResume()
        loadDashboard()
    }

    private fun loadDashboard() {
        viewLifecycleOwner.lifecycleScope.launch {
            val products = ServiceLocator.productRepository.getProducts().getOrDefault(emptyList())
            val orders = ServiceLocator.orderRepository.getAllOrders().getOrDefault(emptyList())
            val users = ServiceLocator.profileRepository.getAllUsers().getOrDefault(emptyList())

            binding.tvProductCount.text = products.size.toString()
            binding.tvOrderCount.text = orders.size.toString()
            binding.tvUserCount.text = users.size.toString()
            binding.tvRevenue.text = orders.sumOf { it.totalAmount }.toCompactVnd()

            renderChart(orders)
            renderRecentOrders(orders, users.associate { it.uid to it.fullName })
        }
    }

    private fun renderChart(orders: List<Order>) {
        binding.llChart.removeAllViews()
        val now = Calendar.getInstance()
        val monthKeys = (5 downTo 0).map { offset ->
            (now.clone() as Calendar).apply { add(Calendar.MONTH, -offset) }
        }
        val revenueByMonth = monthKeys.map { cal ->
            orders.filter {
                val orderCal = Calendar.getInstance().apply { timeInMillis = it.createdAt }
                orderCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                    orderCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
            }.sumOf { it.totalAmount }
        }
        val maxRevenue = revenueByMonth.maxOrNull()?.takeIf { it > 0 } ?: 1.0
        val maxBarHeightDp = 90
        val density = resources.displayMetrics.density

        monthKeys.forEachIndexed { index, cal ->
            val column = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            }
            val barHeightPx = (maxBarHeightDp * density * (revenueByMonth[index] / maxRevenue)).toInt()
                .coerceAtLeast((4 * density).toInt())
            val bar = View(requireContext()).apply {
                setBackgroundColor(resources.getColor(R.color.pp_primary, null))
                layoutParams = LinearLayout.LayoutParams((20 * density).toInt(), barHeightPx)
            }
            val label = TextView(requireContext()).apply {
                text = "T${cal.get(Calendar.MONTH) + 1}"
                textSize = 11f
                setTextColor(resources.getColor(R.color.pp_text_secondary, null))
                gravity = Gravity.CENTER
                setPadding(0, (4 * density).toInt(), 0, 0)
            }
            column.addView(bar)
            column.addView(label)
            binding.llChart.addView(column)
        }
        if (orders.isEmpty()) {
            binding.llChart.removeAllViews()
            binding.llChart.addView(
                TextView(requireContext()).apply {
                    text = "Chưa có dữ liệu doanh thu"
                    setTextColor(Color.parseColor("#8A7B6A"))
                    textSize = 13f
                    gravity = Gravity.CENTER
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                }
            )
        }
    }

    private fun renderRecentOrders(orders: List<Order>, userNames: Map<String, String>) {
        binding.llRecentOrders.removeAllViews()
        val recent = orders.sortedByDescending { it.createdAt }.take(5)
        binding.tvNoOrders.visibility = if (recent.isEmpty()) View.VISIBLE else View.GONE

        recent.forEach { order ->
            val rowBinding = ItemAdminOrderBinding.inflate(layoutInflater, binding.llRecentOrders, false)
            rowBinding.tvCustomerName.text = userNames[order.userId] ?: order.shippingAddress.fullName.ifBlank { "Khách hàng" }
            rowBinding.tvOrderCode.text = order.orderCode.ifBlank { "#${order.id.take(8)}" }
            rowBinding.tvItemCount.text = "${order.items.sumOf { it.quantity }} sản phẩm"
            rowBinding.tvTotal.text = order.totalAmount.toVndString()
            val style = orderStatusStyle(order.status)
            rowBinding.tvStatus.text = style.label
            rowBinding.tvStatus.setTextColor(resources.getColor(style.textColorRes, null))
            rowBinding.tvStatus.backgroundTintList = resources.getColorStateList(style.bgColorRes, null)
            rowBinding.root.setOnClickListener {
                findNavController().navigate(R.id.adminOrdersFragment)
            }
            (rowBinding.root.layoutParams as? LinearLayout.LayoutParams)?.bottomMargin =
                (8 * resources.displayMetrics.density).toInt()
            binding.llRecentOrders.addView(rowBinding.root)
        }
    }
}
