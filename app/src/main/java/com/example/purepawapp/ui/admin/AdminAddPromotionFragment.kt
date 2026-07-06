package com.example.purepawapp.ui.admin

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.data.model.Promotion
import com.example.purepawapp.databinding.FragmentAdminAddPromotionBinding
import com.example.purepawapp.di.ServiceLocator
import com.example.purepawapp.ui.common.BaseFragment
import com.example.purepawapp.util.DiscountType
import com.example.purepawapp.util.toast
import kotlinx.coroutines.launch

private const val THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000

class AdminAddPromotionFragment : BaseFragment<FragmentAdminAddPromotionBinding>(FragmentAdminAddPromotionBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val code = binding.etCode.text?.toString().orEmpty().trim()
            val description = binding.etDescription.text?.toString().orEmpty().trim()
            val value = binding.etValue.text?.toString()?.toDoubleOrNull()
            val minOrder = binding.etMinOrder.text?.toString()?.toDoubleOrNull() ?: 0.0
            val usageLimit = binding.etUsageLimit.text?.toString()?.toIntOrNull() ?: 0
            val discountType = if (binding.chipGroupDiscountType.checkedChipId == binding.chipFixedAmount.id) {
                DiscountType.FIXED_AMOUNT
            } else {
                DiscountType.PERCENTAGE
            }

            if (code.isEmpty() || value == null) {
                toast("Vui lòng nhập mã và giá trị giảm giá")
                return@setOnClickListener
            }

            binding.btnSave.isEnabled = false
            val now = System.currentTimeMillis()
            val promotion = Promotion(
                code = code,
                description = description,
                discountType = discountType,
                discountValue = value,
                minOrderAmount = minOrder,
                usageLimit = usageLimit,
                startDate = now,
                endDate = now + THIRTY_DAYS_MS,
                isActive = true
            )

            viewLifecycleOwner.lifecycleScope.launch {
                ServiceLocator.promotionRepository.addPromotion(promotion)
                    .onSuccess {
                        toast("Đã thêm mã khuyến mãi")
                        findNavController().popBackStack()
                    }
                    .onFailure {
                        binding.btnSave.isEnabled = true
                        toast(it.message ?: "Không thể thêm mã khuyến mãi")
                    }
            }
        }
    }
}
