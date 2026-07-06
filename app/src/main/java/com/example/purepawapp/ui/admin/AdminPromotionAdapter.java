package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.Promotion;
import com.example.purepawapp.databinding.ItemAdminPromotionBinding;
import com.example.purepawapp.util.CurrencyUtils;
import com.example.purepawapp.util.DiscountType;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdminPromotionAdapter extends ListAdapter<Promotion, AdminPromotionAdapter.ViewHolder> {

    private final BiConsumer<Promotion, Boolean> onToggleActive;
    private final Consumer<Promotion> onDelete;

    private static final DiffUtil.ItemCallback<Promotion> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Promotion oldItem, @NonNull Promotion newItem) {
            return oldItem.getCode().equals(newItem.getCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Promotion oldItem, @NonNull Promotion newItem) {
            return oldItem.isActive() == newItem.isActive() && oldItem.getUsedCount() == newItem.getUsedCount();
        }
    };

    public AdminPromotionAdapter(BiConsumer<Promotion, Boolean> onToggleActive, Consumer<Promotion> onDelete) {
        super(DIFF_CALLBACK);
        this.onToggleActive = onToggleActive;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminPromotionBinding binding = ItemAdminPromotionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminPromotionBinding binding;

        ViewHolder(ItemAdminPromotionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Promotion promotion) {
            binding.tvCode.setText(promotion.getCode());
            binding.tvDescription.setText(promotion.getDescription());
            binding.tvValue.setText(DiscountType.PERCENTAGE.equals(promotion.getDiscountType())
                    ? "Giảm " + (int) promotion.getDiscountValue() + "%"
                    : "Giảm " + CurrencyUtils.toVndString(promotion.getDiscountValue()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
            binding.tvDateRange.setText(format.format(promotion.getStartDate()) + " - " + format.format(promotion.getEndDate()));

            int usageLimit = promotion.getUsageLimit();
            binding.progressUsage.setMax(100);
            binding.progressUsage.setProgress(usageLimit > 0
                    ? Math.min(Math.max(promotion.getUsedCount() * 100 / usageLimit, 0), 100)
                    : 0);
            binding.tvUsage.setText(usageLimit > 0
                    ? promotion.getUsedCount() + " / " + usageLimit + " lượt dùng"
                    : promotion.getUsedCount() + " lượt dùng · không giới hạn");

            binding.switchActive.setOnCheckedChangeListener(null);
            binding.switchActive.setChecked(promotion.isActive());
            binding.switchActive.setOnCheckedChangeListener((btn, isChecked) -> onToggleActive.accept(promotion, isChecked));
            binding.btnDelete.setOnClickListener(v -> onDelete.accept(promotion));
        }
    }
}
