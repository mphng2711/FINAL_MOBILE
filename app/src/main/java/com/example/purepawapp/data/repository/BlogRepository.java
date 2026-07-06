package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.BlogPost;

import java.util.List;

public interface BlogRepository {
    void getBlogs(RepoCallback<List<BlogPost>> callback);

    void getBlog(String blogId, RepoCallback<BlogPost> callback);

    void createBlog(BlogPost blog, RepoCallback<Void> callback);

    void deleteBlog(String blogId, RepoCallback<Void> callback);
}
