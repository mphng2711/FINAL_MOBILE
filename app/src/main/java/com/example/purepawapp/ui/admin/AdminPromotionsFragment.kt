package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Promotion
import com.example.purepawapp.databinding.FragmentAdminPromotionsBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.SpacingItemDecoration
import com.example.purepawapp.util.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

private const val SEVEN_DAYS_MS = 7L * 24 * 60 * 60 * 1000

class AdminPromotionsFragment : BaseFragment<FragmentAdminPromotionsBinding>(FragmentAdminPromotionsBinding::inflate) {

    private val adapter = AdminPromotionAdapter(
        onToggleActive = { promotion, isActive -> toggleActive(promotion, isActive) },
        onDelete = { promotion ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xóa mã khuyến mãi")
                .setMessage("Bạn có chắc muốn xóa mã \"${promotion.code}\"?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa") { _, _ -> deletePromotion(promotion.code) }
                .show()
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvPromotions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPromotions.adapter = adapter
        binding.rvPromotions.addItemDecoration(SpacingItemDecoration((12 * resources.displayMetrics.density).toInt()))

        binding.btnAddPromotion.setOnClickListener {
            findNavController().navigate(R.id.action_adminPromotionsFragment_to_adminAddPromotionFragment)
        }

        loadPromotions()
    }

    override fun onResume() {
        super.onResume()
        loadPromotions()
    }

    private fun loadPromotions() {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.promotionRepository.getPromotions()
                .onSuccess { promotions ->
                    adapter.submitList(promotions)
                    binding.emptyState.visibility = if (promotions.isEmpty()) View.VISIBLE else View.GONE

                    val now = System.currentTimeMillis()
                    binding.tvActiveCount.text = promotions.count { it.isActive }.toString()
                    binding.tvUsedCount.text = promotions.sumOf { it.usedCount }.toString()
                    binding.tvExpiringCount.text = promotions.count {
                        it.isActive && it.endDate in now..(now + SEVEN_DAYS_MS)
                    }.toString()
                }
                .onFailure { toast(it.message ?: "Không thể tải danh sách khuyến mãi") }
        }
    }

    private fun toggleActive(promotion: Promotion, isActive: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.promotionRepository.addPromotion(promotion.copy(isActive = isActive))
                .onFailure {
                    toast(it.message ?: "Không thể cập nhật khuyến mãi")
                    loadPromotions()
                }
        }
    }

    private fun deletePromotion(code: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            ServiceLocator.promotionRepository.deletePromotion(code)
                .onSuccess {
                    toast("Đã xóa mã khuyến mãi")
                    loadPromotions()
                }
                .onFailure { toast(it.message ?: "Không thể xóa mã khuyến mãi") }
        }
    }
}
