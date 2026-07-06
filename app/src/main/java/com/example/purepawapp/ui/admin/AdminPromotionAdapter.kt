package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.data.model.Promotion
import com.example.purepawapp.databinding.ItemAdminPromotionBinding
import com.example.purepawapp.util.DiscountType
import com.example.purepawapp.util.toVndString
import java.text.SimpleDateFormat
import java.util.Locale

class AdminPromotionAdapter(
    private val onToggleActive: (Promotion, Boolean) -> Unit,
    private val onDelete: (Promotion) -> Unit
) : ListAdapter<Promotion, AdminPromotionAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminPromotionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(promotion: Promotion) {
            binding.tvCode.text = promotion.code
            binding.tvDescription.text = promotion.description
            binding.tvValue.text = if (promotion.discountType == DiscountType.PERCENTAGE) {
                "Giảm ${promotion.discountValue.toInt()}%"
            } else {
                "Giảm ${promotion.discountValue.toVndString()}"
            }
            val format = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
            binding.tvDateRange.text = "${format.format(promotion.startDate)} - ${format.format(promotion.endDate)}"

            val usageLimit = promotion.usageLimit
            binding.progressUsage.max = 100
            binding.progressUsage.progress = if (usageLimit > 0) {
                (promotion.usedCount * 100 / usageLimit).coerceIn(0, 100)
            } else {
                0
            }
            binding.tvUsage.text = if (usageLimit > 0) {
                "${promotion.usedCount} / $usageLimit lượt dùng"
            } else {
                "${promotion.usedCount} lượt dùng · không giới hạn"
            }

            binding.switchActive.setOnCheckedChangeListener(null)
            binding.switchActive.isChecked = promotion.isActive
            binding.switchActive.setOnCheckedChangeListener { _, isChecked -> onToggleActive(promotion, isChecked) }
            binding.btnDelete.setOnClickListener { onDelete(promotion) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Promotion>() {
            override fun areItemsTheSame(oldItem: Promotion, newItem: Promotion): Boolean = oldItem.code == newItem.code
            override fun areContentsTheSame(oldItem: Promotion, newItem: Promotion): Boolean = oldItem == newItem
        }
    }
}
