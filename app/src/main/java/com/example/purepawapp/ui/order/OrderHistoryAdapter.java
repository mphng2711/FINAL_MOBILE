package com.example.purepawapp.ui.order;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.databinding.ItemOrderHistoryBinding;
import com.example.purepawapp.ui.admin.AdminStatusUi;
import com.example.purepawapp.ui.admin.StatusStyle;
import com.example.purepawapp.util.CurrencyUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class OrderHistoryAdapter extends ListAdapter<Order, OrderHistoryAdapter.ViewHolder> {

    private final Consumer<Order> onClick;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd 'Tháng' MM, yyyy", new Locale("vi", "VN"));

    private static final DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getStatus().equals(newItem.getStatus()) && oldItem.getTotalAmount() == newItem.getTotalAmount();
        }
    };

    public OrderHistoryAdapter(Consumer<Order> onClick) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryBinding binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderHistoryBinding binding;

        ViewHolder(ItemOrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            String orderCode = order.getOrderCode();
            binding.tvOrderId.setText(orderCode != null && !orderCode.isBlank() ? orderCode : "#" + order.getId().substring(0, Math.min(8, order.getId().length())));
            binding.tvDate.setText(dateFormat.format(order.getCreatedAt()));

            StatusStyle style = AdminStatusUi.orderStatusStyle(order.getStatus());
            binding.tvStatus.setText(style.getLabel());
            binding.tvStatus.setTextColor(binding.getRoot().getResources().getColor(style.getTextColorRes(), null));
            binding.tvStatus.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(style.getBgColorRes(), null));

            int itemCount = 0;
            for (var item : order.getItems()) itemCount += item.getQuantity();
            String prefix = itemCount + " sản phẩm • ";
            String full = prefix + CurrencyUtils.toVndString(order.getTotalAmount());
            SpannableString spannable = new SpannableString(full);
            spannable.setSpan(new ForegroundColorSpan(binding.getRoot().getResources().getColor(R.color.pp_primary, null)),
                    prefix.length(), full.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), full.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.tvSummary.setText(spannable);
            binding.tvSummary.setTextColor(binding.getRoot().getResources().getColor(R.color.pp_text_primary, null));

            binding.getRoot().setOnClickListener(v -> onClick.accept(order));
        }
    }
}
