package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.data.model.BlogPost;
import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.databinding.FragmentAdminAddBlogBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.example.purepawapp.util.ViewUtils;

public class AdminAddBlogFragment extends BaseFragment<FragmentAdminAddBlogBinding> {

    public AdminAddBlogFragment() {
        super(FragmentAdminAddBlogBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        getBinding().btnSave.setOnClickListener(v -> {
            String title = text(getBinding().etTitle).trim();
            String author = text(getBinding().etAuthor).trim();
            String content = text(getBinding().etContent).trim();

            if (title.isEmpty() || content.isEmpty()) {
                ViewUtils.toast(this, "Vui lòng nhập tiêu đề và nội dung");
                return;
            }

            getBinding().btnSave.setEnabled(false);
            BlogPost blog = new BlogPost();
            blog.setTitle(title);
            blog.setAuthor(author.isBlank() ? "Đội ngũ PURE PAW" : author);
            blog.setContentHtml("<p>" + content.replace("\n", "</p><p>") + "</p>");
            blog.setStatus(getBinding().switchPublishNow.isChecked() ? "published" : "draft");
            blog.setPublishedAt(System.currentTimeMillis());

            ServiceLocator.getBlogRepository().createBlog(blog, new RepoCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    ViewUtils.toast(AdminAddBlogFragment.this, "Đã đăng bài viết");
                    NavHostFragment.findNavController(AdminAddBlogFragment.this).popBackStack();
                }

                @Override
                public void onError(Exception error) {
                    getBinding().btnSave.setEnabled(true);
                    ViewUtils.toast(AdminAddBlogFragment.this, error.getMessage() != null ? error.getMessage() : "Không thể đăng bài viết");
                }
            });
        });
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString() : "";
    }
}
