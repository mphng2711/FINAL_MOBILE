package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.databinding.ItemAdminOrderBinding;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.Map;
import java.util.function.Consumer;

public class AdminOrderAdapter extends ListAdapter<Order, AdminOrderAdapter.ViewHolder> {

    private final Consumer<Order> onClick;
    private Map<String, String> userNames = Map.of();

    private static final DiffUtil.ItemCallback<Order> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    public AdminOrderAdapter(Consumer<Order> onClick) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
    }

    public void updateUserNames(Map<String, String> names) {
        userNames = names;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOrderBinding binding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminOrderBinding binding;

        ViewHolder(ItemAdminOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            String customerName = userNames.get(order.getUserId());
            if (customerName == null || customerName.isBlank()) {
                customerName = order.getShippingAddress().getFullName();
                if (customerName == null || customerName.isBlank()) customerName = "Khách hàng";
            }
            binding.tvCustomerName.setText(customerName);
            String orderCode = order.getOrderCode();
            binding.tvOrderCode.setText(orderCode != null && !orderCode.isBlank() ? orderCode : "#" + order.getId().substring(0, Math.min(8, order.getId().length())));
            int itemCount = 0;
            for (var item : order.getItems()) itemCount += item.getQuantity();
            binding.tvItemCount.setText(itemCount + " sản phẩm");
            binding.tvTotal.setText(CurrencyUtils.toVndString(order.getTotalAmount()));
            StatusStyle style = AdminStatusUi.orderStatusStyle(order.getStatus());
            binding.tvStatus.setText(style.getLabel());
            binding.tvStatus.setTextColor(binding.getRoot().getResources().getColor(style.getTextColorRes(), null));
            binding.tvStatus.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(style.getBgColorRes(), null));
            binding.getRoot().setOnClickListener(v -> onClick.accept(order));
        }
    }
}
