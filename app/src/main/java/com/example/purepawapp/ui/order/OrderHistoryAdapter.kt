package com.example.purepawapp.ui.order

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.R
import com.example.purepawapp.data.model.Order
import com.example.purepawapp.databinding.ItemOrderHistoryBinding
import com.example.purepawapp.ui.admin.orderStatusStyle
import com.example.purepawapp.util.toVndString
import java.text.SimpleDateFormat
import java.util.Locale

class OrderHistoryAdapter(
    private val onClick: (Order) -> Unit
) : ListAdapter<Order, OrderHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val dateFormat = SimpleDateFormat("dd 'Tháng' MM, yyyy", Locale("vi", "VN"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvOrderId.text = order.orderCode.ifBlank { "#${order.id.take(8)}" }
            binding.tvDate.text = dateFormat.format(order.createdAt)

            val style = orderStatusStyle(order.status)
            binding.tvStatus.text = style.label
            binding.tvStatus.setTextColor(binding.root.resources.getColor(style.textColorRes, null))
            binding.tvStatus.backgroundTintList = binding.root.resources.getColorStateList(style.bgColorRes, null)

            val itemCount = order.items.sumOf { it.quantity }
            val prefix = "$itemCount sản phẩm • "
            val full = prefix + order.totalAmount.toVndString()
            val spannable = SpannableString(full)
            spannable.setSpan(
                ForegroundColorSpan(binding.root.resources.getColor(R.color.pp_primary, null)),
                prefix.length,
                full.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                StyleSpan(android.graphics.Typeface.BOLD),
                prefix.length,
                full.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.tvSummary.text = spannable
            binding.tvSummary.setTextColor(binding.root.resources.getColor(R.color.pp_text_primary, null))

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
