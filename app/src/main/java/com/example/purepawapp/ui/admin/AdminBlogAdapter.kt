package com.example.purepawapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.purepawapp.R
import com.example.purepawapp.data.model.BlogPost
import com.example.purepawapp.databinding.ItemAdminBlogBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdminBlogAdapter(
    private val onDelete: (BlogPost) -> Unit
) : ListAdapter<BlogPost, AdminBlogAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAdminBlogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blog: BlogPost) {
            binding.tvTitle.text = blog.title
            val format = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
            binding.tvAuthor.text = "${blog.author} · ${format.format(blog.publishedAt)}"

            val isPublished = blog.status == "published"
            binding.tvStatus.text = if (isPublished) "Đã đăng" else "Nháp"
            binding.tvStatus.setTextColor(
                binding.root.resources.getColor(
                    if (isPublished) R.color.pp_success_dark else R.color.pp_text_secondary,
                    null
                )
            )
            binding.tvStatus.backgroundTintList = binding.root.resources.getColorStateList(
                if (isPublished) R.color.pp_chip_green_bg else R.color.pp_chip_gray_bg,
                null
            )

            binding.btnDelete.setOnClickListener { onDelete(blog) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {
            override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean = oldItem == newItem
        }
    }
}
