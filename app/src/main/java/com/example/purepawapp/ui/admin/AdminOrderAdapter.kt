package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.data.model.Order
import com.example.purepawapp.databinding.ItemAdminOrderBinding
import com.example.purepawapp.util.toVndString

class AdminOrderAdapter(
    private val onClick: (Order) -> Unit
) : ListAdapter<Order, AdminOrderAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var userNames: Map<String, String> = emptyMap()

    fun updateUserNames(names: Map<String, String>) {
        userNames = names
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvCustomerName.text = userNames[order.userId] ?: order.shippingAddress.fullName.ifBlank { "Khách hàng" }
            binding.tvOrderCode.text = order.orderCode.ifBlank { "#${order.id.take(8)}" }
            binding.tvItemCount.text = "${order.items.sumOf { it.quantity }} sản phẩm"
            binding.tvTotal.text = order.totalAmount.toVndString()
            val style = orderStatusStyle(order.status)
            binding.tvStatus.text = style.label
            binding.tvStatus.setTextColor(binding.root.resources.getColor(style.textColorRes, null))
            binding.tvStatus.backgroundTintList =
                binding.root.resources.getColorStateList(style.bgColorRes, null)
            binding.root.setOnClickListener { onClick(order) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
        }
    }
}
