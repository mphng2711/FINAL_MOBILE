package com.example.purepawapp.ui.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.BlogPost;
import com.example.purepawapp.databinding.ItemAdminBlogBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Consumer;

public class AdminBlogAdapter extends ListAdapter<BlogPost, AdminBlogAdapter.ViewHolder> {

    private final Consumer<BlogPost> onDelete;

    private static final DiffUtil.ItemCallback<BlogPost> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull BlogPost oldItem, @NonNull BlogPost newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull BlogPost oldItem, @NonNull BlogPost newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    public AdminBlogAdapter(Consumer<BlogPost> onDelete) {
        super(DIFF_CALLBACK);
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminBlogBinding binding = ItemAdminBlogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminBlogBinding binding;

        ViewHolder(ItemAdminBlogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BlogPost blog) {
            binding.tvTitle.setText(blog.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
            binding.tvAuthor.setText(blog.getAuthor() + " · " + format.format(blog.getPublishedAt()));

            boolean isPublished = "published".equals(blog.getStatus());
            binding.tvStatus.setText(isPublished ? "Đã đăng" : "Nháp");
            binding.tvStatus.setTextColor(binding.getRoot().getResources().getColor(
                    isPublished ? R.color.pp_success_dark : R.color.pp_text_secondary, null));
            binding.tvStatus.setBackgroundTintList(binding.getRoot().getResources().getColorStateList(
                    isPublished ? R.color.pp_chip_green_bg : R.color.pp_chip_gray_bg, null));

            binding.btnDelete.setOnClickListener(v -> onDelete.accept(blog));
        }
    }
}
