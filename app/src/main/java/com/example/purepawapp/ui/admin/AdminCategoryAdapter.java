package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.Category;
import com.example.purepawapp.databinding.ItemAdminCategoryBinding;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdminCategoryAdapter extends ListAdapter<CategoryRow, AdminCategoryAdapter.ViewHolder> {

    private final BiConsumer<Category, Boolean> onToggleActive;
    private final Consumer<Category> onEdit;
    private final Consumer<Category> onDelete;

    private static final DiffUtil.ItemCallback<CategoryRow> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryRow oldItem, @NonNull CategoryRow newItem) {
            return oldItem.getCategory().getId().equals(newItem.getCategory().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryRow oldItem, @NonNull CategoryRow newItem) {
            return oldItem.getProductCount() == newItem.getProductCount()
                    && oldItem.getCategory().isActive() == newItem.getCategory().isActive()
                    && oldItem.getCategory().getName().equals(newItem.getCategory().getName());
        }
    };

    public AdminCategoryAdapter(BiConsumer<Category, Boolean> onToggleActive, Consumer<Category> onEdit, Consumer<Category> onDelete) {
        super(DIFF_CALLBACK);
        this.onToggleActive = onToggleActive;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminCategoryBinding binding = ItemAdminCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminCategoryBinding binding;

        ViewHolder(ItemAdminCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CategoryRow row) {
            binding.tvIcon.setText(AdminStatusUi.categoryEmoji(row.getCategory()));
            binding.tvName.setText(row.getCategory().getName());
            binding.tvProductCount.setText(row.getProductCount() + " sản phẩm");
            binding.switchActive.setOnCheckedChangeListener(null);
            binding.switchActive.setChecked(row.getCategory().isActive());
            binding.switchActive.setOnCheckedChangeListener((btn, isChecked) -> onToggleActive.accept(row.getCategory(), isChecked));
            binding.btnEdit.setOnClickListener(v -> onEdit.accept(row.getCategory()));
            binding.btnDelete.setOnClickListener(v -> onDelete.accept(row.getCategory()));
        }
    }
}
