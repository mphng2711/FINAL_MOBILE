package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.purepawapp.R;
import com.example.purepawapp.data.model.BlogPost;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminBlogsBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.SpacingItemDecoration;
import com.example.purepawapp.util.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AdminBlogsFragment extends BaseFragment<FragmentAdminBlogsBinding> {

    private List<BlogPost> allBlogs = List.of();

    private final AdminBlogAdapter adapter = new AdminBlogAdapter(blog ->
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Xóa bài viết")
                    .setMessage("Bạn có chắc muốn xóa \"" + blog.getTitle() + "\"?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, which) -> deleteBlog(blog.getId()))
                    .show());

    public AdminBlogsFragment() {
        super(FragmentAdminBlogsBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
        getBinding().rvBlogs.setLayoutManager(new LinearLayoutManager(requireContext()));
        getBinding().rvBlogs.setAdapter(adapter);
        getBinding().rvBlogs.addItemDecoration(new SpacingItemDecoration((int) (12 * getResources().getDisplayMetrics().density)));

        getBinding().btnAddBlog.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_adminBlogsFragment_to_adminAddBlogFragment));
        getBinding().chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilter());

        loadBlogs();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBlogs();
    }

    private void loadBlogs() {
        ServiceLocator.getBlogRepository().getBlogs(new RepoCallback<>() {
            @Override
            public void onSuccess(List<BlogPost> result) {
                allBlogs = result;
                applyFilter();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminBlogsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể tải danh sách blog");
            }
        });
    }

    private void applyFilter() {
        int checkedId = getBinding().chipGroupFilter.getCheckedChipId();
        List<BlogPost> filtered;
        if (checkedId == getBinding().chipPublished.getId()) {
            filtered = filterByStatus("published");
        } else if (checkedId == getBinding().chipDraft.getId()) {
            filtered = filterByStatus("draft");
        } else {
            filtered = allBlogs;
        }
        adapter.submitList(filtered);
        getBinding().emptyState.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private List<BlogPost> filterByStatus(String status) {
        List<BlogPost> result = new ArrayList<>();
        for (BlogPost b : allBlogs) if (status.equals(b.getStatus())) result.add(b);
        return result;
    }

    private void deleteBlog(String blogId) {
        ServiceLocator.getBlogRepository().deleteBlog(blogId, new RepoCallback<>() {
            @Override
            public void onSuccess(Void result) {
                ViewUtils.toast(AdminBlogsFragment.this, "Đã xóa bài viết");
                loadBlogs();
            }

            @Override
            public void onError(Exception error) {
                ViewUtils.toast(AdminBlogsFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể xóa bài viết");
            }
        });
    }
}
