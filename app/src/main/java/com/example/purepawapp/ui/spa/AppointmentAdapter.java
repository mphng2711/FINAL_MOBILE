package com.example.purepawapp.ui.spa;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.databinding.ItemAppointmentHistoryBinding;
import com.example.purepawapp.ui.admin.AdminStatusUi;
import com.example.purepawapp.ui.admin.StatusStyle;
import com.example.purepawapp.util.BookingStatus;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.Set;
import java.util.function.Consumer;

public class AppointmentAdapter extends ListAdapter<Booking, AppointmentAdapter.ViewHolder> {

    private static final Set<String> CANCELLABLE_STATUSES = Set.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final Consumer<Booking> onClick;
    private final Consumer<Booking> onCancel;

    private static final DiffUtil.ItemCallback<Booking> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.getStatus().equals(newItem.getStatus()) && oldItem.getRating() == newItem.getRating();
        }
    };

    public AppointmentAdapter(Consumer<Booking> onClick, Consumer<Booking> onCancel) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
        this.onCancel = onCancel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentHistoryBinding binding = ItemAppointmentHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAppointmentHistoryBinding binding;

        ViewHolder(ItemAppointmentHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Booking booking) {
            binding.tvDate.setText("📅 " + booking.getBookingDate() + " · " + booking.getTimeSlot());
            String code = booking.getBookingCode();
            binding.tvCode.setText(code != null && !code.isBlank() ? code : "#" + booking.getId().substring(0, Math.min(8, booking.getId().length())));
            binding.tvService.setText(booking.getServiceName());
            String petLabel = booking.getPet() == null ? "" : booking.getPet().getName()
                    + (booking.getPet().getSpecies().isEmpty() ? "" : " (" + booking.getPet().getSpecies() + ")");
            binding.tvPet.setText(petLabel);
            binding.tvPrice.setText(CurrencyUtils.toVndString(booking.getPrice()));

            String icon = "grooming".equals(booking.getServiceType()) ? "✂️" : "wellness".equals(booking.getServiceType()) ? "🏥" : "🐾";
            binding.tvIcon.setText(icon);
            binding.tvIcon.setBackgroundTintList(ColorStateList.valueOf(
                    binding.getRoot().getResources().getColor(R.color.pp_chip_pink_bg, null)));

            if (booking.getRating() > 0) {
                binding.tvStars.setVisibility(android.view.View.VISIBLE);
                binding.tvStars.setText("⭐".repeat(booking.getRating()) + "☆".repeat(5 - booking.getRating()));
            } else {
                binding.tvStars.setVisibility(android.view.View.GONE);
            }

            StatusStyle style = AdminStatusUi.bookingStatusStyle(booking.getStatus());
            binding.tvStatus.setText(style.getLabel());
            binding.tvStatus.setTextColor(binding.getRoot().getResources().getColor(style.getTextColorRes(), null));
            binding.tvStatus.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(style.getBgColorRes(), null));

            boolean cancellable = CANCELLABLE_STATUSES.contains(booking.getStatus());
            binding.btnCancel.setVisibility(cancellable ? android.view.View.VISIBLE : android.view.View.GONE);
            binding.btnCancel.setOnClickListener(v -> onCancel.accept(booking));

            binding.getRoot().setOnClickListener(v -> onClick.accept(booking));
        }
    }
}
