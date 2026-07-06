package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.R
import com.example.purepawapp.data.model.User
import com.example.purepawapp.databinding.ItemAdminUserBinding

class AdminUserAdapter : ListAdapter<User, AdminUserAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var orderCounts: Map<String, Int> = emptyMap()

    fun updateOrderCounts(counts: Map<String, Int>) {
        orderCounts = counts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvName.text = user.fullName.ifBlank { "(Chưa đặt tên)" }
            binding.tvEmail.text = user.email
            binding.tvOrderCount.text = "${orderCounts[user.uid] ?: 0} đơn hàng"
            val isAdmin = user.role == "admin"
            binding.tvRole.text = if (isAdmin) "Quản trị viên" else "Khách hàng"
            binding.tvRole.setTextColor(
                binding.root.resources.getColor(
                    if (isAdmin) R.color.pp_primary_dark else R.color.pp_status_blue_text,
                    null
                )
            )
            binding.tvRole.backgroundTintList = binding.root.resources.getColorStateList(
                if (isAdmin) R.color.pp_chip_orange_bg else R.color.pp_status_blue_bg,
                null
            )
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.uid == newItem.uid
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
        }
    }
}
