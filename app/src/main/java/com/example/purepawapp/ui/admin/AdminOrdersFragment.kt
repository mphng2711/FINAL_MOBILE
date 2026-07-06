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
import com.example.purepawapp.data.model.Order
import com.example.purepawapp.databinding.FragmentAdminOrdersBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.OrderStatus
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

private val PENDING_STATUSES = setOf(
    OrderStatus.PENDING,
    OrderStatus.CONFIRMED,
    OrderStatus.PROCESSING,
    OrderStatus.SHIPPING
)

class AdminOrdersFragment : BaseFragment<FragmentAdminOrdersBinding>(FragmentAdminOrdersBinding::inflate) {

    private var allOrders: List<Order> = emptyList()
    private var userNames: Map<String, String> = emptyMap()

    private val adapter = AdminOrderAdapter(
        onClick = { order ->
            findNavController().navigate(
                R.id.action_adminOrdersFragment_to_adminOrderDetailFragment,
                bundleOf("orderId" to order.id)
            )
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
        binding.rvOrders.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = applyFilter()
            override fun afterTextChanged(s: Editable?) = Unit
        })
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, _ -> applyFilter() }

        loadOrders()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            val usersResult = ServiceLocator.profileRepository.getAllUsers()
            usersResult.onSuccess { users -> userNames = users.associate { it.uid to it.fullName } }
            ServiceLocator.orderRepository.getAllOrders()
                .onSuccess { orders ->
                    allOrders = orders
                    adapter.updateUserNames(userNames)
                    applyFilter()
                }
                .onFailure { toast(it.message ?: "Không thể tải danh sách đơn hàng") }
        }
    }

    private fun applyFilter() {
        val query = binding.etSearch.text?.toString().orEmpty().trim()
        var filtered = allOrders
        filtered = when (binding.chipGroupFilter.checkedChipId) {
            binding.chipPending.id -> filtered.filter { it.status in PENDING_STATUSES }
            binding.chipCompleted.id -> filtered.filter { it.status == OrderStatus.DELIVERED }
            else -> filtered
        }
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.orderCode.contains(query, ignoreCase = true) ||
                    (userNames[it.userId] ?: it.shippingAddress.fullName).contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filtered)
        binding.emptyState.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }
}
