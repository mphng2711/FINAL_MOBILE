package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.data.model.Booking
import com.example.purepawapp.databinding.ItemAdminBookingBinding
import com.example.purepawapp.util.toVndString

class AdminBookingAdapter(
    private val onClick: (Booking) -> Unit
) : ListAdapter<Booking, AdminBookingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.tvServiceName.text = booking.serviceName
            binding.tvPetAndDate.text = "${booking.pet.name} · ${booking.bookingDate} ${booking.timeSlot}"
            binding.tvPrice.text = booking.price.toVndString()
            val style = bookingStatusStyle(booking.status)
            binding.tvStatus.text = style.label
            binding.tvStatus.setTextColor(binding.root.resources.getColor(style.textColorRes, null))
            binding.tvStatus.backgroundTintList =
                binding.root.resources.getColorStateList(style.bgColorRes, null)
            binding.root.setOnClickListener { onClick(booking) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Booking>() {
            override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean = oldItem == newItem
        }
    }
}
