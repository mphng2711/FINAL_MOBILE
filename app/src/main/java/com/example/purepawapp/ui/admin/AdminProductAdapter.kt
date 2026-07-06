package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.databinding.ItemAdminProductBinding
import com.example.purepawapp.ui.product.emoji
import com.example.purepawapp.util.toVndString

class AdminProductAdapter(
    private val onDelete: (Product) -> Unit
) : ListAdapter<Product, AdminProductAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvIcon.text = product.emoji
            binding.tvName.text = product.name
            binding.tvCategory.text = categoryDisplayName(product.categoryId)
            binding.tvPrice.text = product.displayPrice.toVndString()

            val totalStock = product.variants.sumOf { it.stock }
            val inStock = totalStock > 0
            binding.tvStockStatus.text = if (inStock) "Còn hàng" else "Hết hàng"
            binding.tvStockStatus.setTextColor(
                binding.root.resources.getColor(
                    if (inStock) R.color.pp_success_dark else R.color.pp_status_red_text,
                    null
                )
            )
            binding.tvStockStatus.backgroundTintList = binding.root.resources.getColorStateList(
                if (inStock) R.color.pp_chip_green_bg else R.color.pp_status_red_bg,
                null
            )

            binding.btnDelete.setOnClickListener { onDelete(product) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
        }
    }
}
