package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.databinding.ItemAdminBookingBinding;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.function.Consumer;

public class AdminBookingAdapter extends ListAdapter<Booking, AdminBookingAdapter.ViewHolder> {

    private final Consumer<Booking> onClick;

    private static final DiffUtil.ItemCallback<Booking> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    public AdminBookingAdapter(Consumer<Booking> onClick) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminBookingBinding binding = ItemAdminBookingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminBookingBinding binding;

        ViewHolder(ItemAdminBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Booking booking) {
            binding.tvServiceName.setText(booking.getServiceName());
            binding.tvPetAndDate.setText(booking.getPet().getName() + " · " + booking.getBookingDate() + " " + booking.getTimeSlot());
            binding.tvPrice.setText(CurrencyUtils.toVndString(booking.getPrice()));
            StatusStyle style = AdminStatusUi.bookingStatusStyle(booking.getStatus());
            binding.tvStatus.setText(style.getLabel());
            binding.tvStatus.setTextColor(binding.getRoot().getResources().getColor(style.getTextColorRes(), null));
            binding.tvStatus.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(style.getBgColorRes(), null));
            binding.getRoot().setOnClickListener(v -> onClick.accept(booking));
        }
    }
}
