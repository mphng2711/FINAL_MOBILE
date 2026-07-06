package com.example.purepawapp.ui.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.databinding.ItemProductRowBinding;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.function.Consumer;

public class ProductRowAdapter extends ListAdapter<Product, ProductRowAdapter.ViewHolder> {

    private final Consumer<Product> onClick;

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

    public ProductRowAdapter(Consumer<Product> onClick) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductRowBinding binding = ItemProductRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductRowBinding binding;

        ViewHolder(ItemProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            binding.tvIcon.setText(ProductUi.getEmoji(product));
            binding.tvName.setText(product.getName());
            binding.tvSubtitle.setText(product.getShortDescription());
            binding.tvRating.setText(String.format("⭐ %.1f", product.getAverageRating()));
            binding.tvPrice.setText(CurrencyUtils.toVndString(product.getDisplayPrice()));
            binding.getRoot().setOnClickListener(v -> onClick.accept(product));
        }
    }
}
