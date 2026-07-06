package com.example.purepawapp.ui.order

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentOrderHistoryBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding>(FragmentOrderHistoryBinding::inflate) {

    private val adapter = OrderHistoryAdapter(
        onClick = { order ->
            findNavController().navigate(
                R.id.action_orderHistoryFragment_to_orderDetailFragment,
                bundleOf("orderId" to order.id)
            )
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter
        binding.rvOrders.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        loadOrders()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        val uid = ServiceLocator.authRepository.currentUserId ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.orderRepository.getOrders(uid)
                .onSuccess { orders ->
                    adapter.submitList(orders)
                    binding.emptyState.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
                }
                .onFailure { toast(it.message ?: "Không thể tải lịch sử đơn hàng") }
        }
    }
}
