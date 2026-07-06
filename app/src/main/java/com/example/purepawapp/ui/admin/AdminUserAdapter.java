package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.User;
import com.example.purepawapp.databinding.ItemAdminUserBinding;

import java.util.Map;

public class AdminUserAdapter extends ListAdapter<User, AdminUserAdapter.ViewHolder> {

    private Map<String, Integer> orderCounts = Map.of();

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getRole().equals(newItem.getRole()) && oldItem.getFullName().equals(newItem.getFullName());
        }
    };

    public AdminUserAdapter() {
        super(DIFF_CALLBACK);
    }

    public void updateOrderCounts(Map<String, Integer> counts) {
        orderCounts = counts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminUserBinding binding = ItemAdminUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminUserBinding binding;

        ViewHolder(ItemAdminUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(User user) {
            String name = user.getFullName();
            binding.tvName.setText(name == null || name.isBlank() ? "(Chưa đặt tên)" : name);
            binding.tvEmail.setText(user.getEmail());
            Integer count = orderCounts.get(user.getUid());
            binding.tvOrderCount.setText((count == null ? 0 : count) + " đơn hàng");
            boolean isAdmin = "admin".equals(user.getRole());
            binding.tvRole.setText(isAdmin ? "Quản trị viên" : "Khách hàng");
            binding.tvRole.setTextColor(binding.getRoot().getResources().getColor(
                    isAdmin ? R.color.pp_primary_dark : R.color.pp_status_blue_text, null));
            binding.tvRole.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(
                    isAdmin ? R.color.pp_chip_orange_bg : R.color.pp_status_blue_bg, null));
        }
    }
}
