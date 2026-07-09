package com.example.purepawapp.ui.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.Product;
import com.example.purepawapp.databinding.ItemProductCardBinding;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.function.Consumer;

public class ProductCardAdapter extends ListAdapter<Product, ProductCardAdapter.ViewHolder> {

    private final Consumer<Product> onClick;
    private final Consumer<Product> onAddToCart;

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

    public ProductCardAdapter(Consumer<Product> onClick, Consumer<Product> onAddToCart) {
        super(DIFF_CALLBACK);
        this.onClick = onClick;
        this.onAddToCart = onAddToCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductCardBinding binding = ItemProductCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductCardBinding binding;

        ViewHolder(ItemProductCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Product product) {
            ProductUi.loadImage(binding.ivProductImage, product);
            binding.tvBadge.setText(ProductUi.getBadgeLabel(product));
            binding.tvName.setText(product.getName());
            binding.tvPrice.setText(CurrencyUtils.toVndString(product.getDisplayPrice()));
            binding.getRoot().setOnClickListener(v -> onClick.accept(product));
            binding.btnAddToCart.setOnClickListener(v -> onAddToCart.accept(product));
        }
    }
}
