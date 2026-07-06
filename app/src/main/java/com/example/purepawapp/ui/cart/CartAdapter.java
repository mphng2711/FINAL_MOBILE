package com.example.purepawapp.ui.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.data.model.CartItem;
import com.example.purepawapp.databinding.ItemCartBinding;
import com.example.purepawapp.util.CurrencyUtils;

import java.util.function.Consumer;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartViewHolder> {

    private final Consumer<CartItem> onIncrease;
    private final Consumer<CartItem> onDecrease;
    private final Consumer<CartItem> onRemove;

    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getProduct().getId().equals(newItem.getProduct().getId())
                    && oldItem.getVariant().getId().equals(newItem.getVariant().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getQuantity() == newItem.getQuantity();
        }
    };

    public CartAdapter(Consumer<CartItem> onIncrease, Consumer<CartItem> onDecrease, Consumer<CartItem> onRemove) {
        super(DIFF_CALLBACK);
        this.onIncrease = onIncrease;
        this.onDecrease = onDecrease;
        this.onRemove = onRemove;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final ItemCartBinding binding;

        CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(CartItem item) {
            binding.tvName.setText(item.getProduct().getName());
            binding.tvVariant.setText(item.getVariant().getName());
            binding.tvPrice.setText(CurrencyUtils.toVndString(item.getUnitPrice()));
            binding.tvQuantity.setText(String.valueOf(item.getQuantity()));
            binding.btnMinus.setOnClickListener(v -> onDecrease.accept(item));
            binding.btnPlus.setOnClickListener(v -> onIncrease.accept(item));
            binding.btnRemove.setOnClickListener(v -> onRemove.accept(item));
        }
    }
}
