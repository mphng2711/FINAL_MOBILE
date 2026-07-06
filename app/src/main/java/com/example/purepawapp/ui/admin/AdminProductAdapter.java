package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.databinding.ItemAdminProductBinding;
import com.example.purepawapp.ui.product.ProductUi;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.function.Consumer;

public class AdminProductAdapter extends ListAdapter<Product, AdminProductAdapter.ViewHolder> {

    private final Consumer<Product> onDelete;

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.equals(newItem);
        }
    };

    public AdminProductAdapter(Consumer<Product> onDelete) {
        super(DIFF_CALLBACK);
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminProductBinding binding = ItemAdminProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminProductBinding binding;

        ViewHolder(ItemAdminProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            binding.tvIcon.setText(ProductUi.getEmoji(product));
            binding.tvName.setText(product.getName());
            binding.tvCategory.setText(AdminStatusUi.categoryDisplayName(product.getCategoryId()));
            binding.tvPrice.setText(CurrencyUtils.toVndString(product.getDisplayPrice()));

            int totalStock = 0;
            for (var variant : product.getVariants()) totalStock += variant.getStock();
            boolean inStock = totalStock > 0;
            binding.tvStockStatus.setText(inStock ? "Còn hàng" : "Hết hàng");
            binding.tvStockStatus.setTextColor(binding.getRoot().getResources().getColor(
                    inStock ? R.color.pp_success_dark : R.color.pp_status_red_text, null));
            binding.tvStockStatus.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(
                    inStock ? R.color.pp_chip_green_bg : R.color.pp_status_red_bg, null));

            binding.btnDelete.setOnClickListener(v -> onDelete.accept(product));
        }
    }
}
