package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.data.model.User
import com.example.purepawapp.databinding.FragmentAdminUsersBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

private const val SEVEN_DAYS_MS = 7L * 24 * 60 * 60 * 1000

class AdminUsersFragment : BaseFragment<FragmentAdminUsersBinding>(FragmentAdminUsersBinding::inflate) {

    private var allUsers: List<User> = emptyList()
    private var orderCounts: Map<String, Int> = emptyMap()

    private val adapter = AdminUserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter
        binding.rvUsers.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = applyFilter()
            override fun afterTextChanged(s: Editable?) = Unit
        })
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, _ -> applyFilter() }

        loadUsers()
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }

    private fun loadUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            val usersResult = ServiceLocator.profileRepository.getAllUsers()
            val ordersResult = ServiceLocator.orderRepository.getAllOrders()

            orderCounts = ordersResult.getOrDefault(emptyList()).groupingBy { it.userId }.eachCount()
            adapter.updateOrderCounts(orderCounts)

            usersResult
                .onSuccess { users ->
                    allUsers = users
                    val now = System.currentTimeMillis()
                    binding.tvTotalCount.text = users.size.toString()
                    binding.tvNewCount.text = users.count { now - it.createdAt <= SEVEN_DAYS_MS }.toString()
                    binding.tvActiveCount.text = users.count { (orderCounts[it.uid] ?: 0) > 0 }.toString()
                    applyFilter()
                }
                .onFailure { toast(it.message ?: "Không thể tải danh sách người dùng") }
        }
    }

    private fun applyFilter() {
        val query = binding.etSearch.text?.toString().orEmpty().trim()
        var filtered = allUsers
        if (binding.chipGroupFilter.checkedChipId == binding.chipCustomers.id) {
            filtered = filtered.filter { it.role != "admin" }
        }
        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.fullName.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filtered)
    }
}
