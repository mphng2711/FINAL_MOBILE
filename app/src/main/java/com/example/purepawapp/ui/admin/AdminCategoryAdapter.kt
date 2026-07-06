package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.data.model.Category
import com.example.purepawapp.databinding.ItemAdminCategoryBinding

data class CategoryRow(val category: Category, val productCount: Int)

class AdminCategoryAdapter(
    private val onToggleActive: (Category, Boolean) -> Unit,
    private val onEdit: (Category) -> Unit,
    private val onDelete: (Category) -> Unit
) : ListAdapter<CategoryRow, AdminCategoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: CategoryRow) {
            binding.tvIcon.text = categoryEmoji(row.category)
            binding.tvName.text = row.category.name
            binding.tvProductCount.text = "${row.productCount} sản phẩm"
            binding.switchActive.setOnCheckedChangeListener(null)
            binding.switchActive.isChecked = row.category.isActive
            binding.switchActive.setOnCheckedChangeListener { _, isChecked ->
                onToggleActive(row.category, isChecked)
            }
            binding.btnEdit.setOnClickListener { onEdit(row.category) }
            binding.btnDelete.setOnClickListener { onDelete(row.category) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryRow>() {
            override fun areItemsTheSame(oldItem: CategoryRow, newItem: CategoryRow): Boolean =
                oldItem.category.id == newItem.category.id
            override fun areContentsTheSame(oldItem: CategoryRow, newItem: CategoryRow): Boolean = oldItem == newItem
        }
    }
}
