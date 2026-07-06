package com.example.purepawapp.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.data.model.Product
import com.example.purepawapp.databinding.ItemProductRowBinding
import com.example.purepawapp.util.toVndString

class ProductRowAdapter(
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductRowAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemProductRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvIcon.text = product.emoji
            binding.tvName.text = product.name
            binding.tvSubtitle.text = product.shortDescription
            binding.tvRating.text = "⭐ ${"%.1f".format(product.averageRating)}"
            binding.tvPrice.text = product.displayPrice.toVndString()
            binding.root.setOnClickListener { onClick(product) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
        }
    }
}
