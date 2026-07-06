package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.BlogPost;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BlogRepositoryImpl implements BlogRepository {

    private final FirebaseFirestore firestore;

    public BlogRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void getBlogs(RepoCallback<List<BlogPost>> callback) {
        firestore.collection(FirestoreCollections.BLOGS)
                .orderBy("publishedAt")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<BlogPost> blogs = snapshot.toObjects(BlogPost.class);
                    Collections.sort(blogs, Comparator.comparingLong(BlogPost::getPublishedAt).reversed());
                    callback.onSuccess(blogs);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getBlog(String blogId, RepoCallback<BlogPost> callback) {
        firestore.collection(FirestoreCollections.BLOGS).document(blogId).get()
                .addOnSuccessListener(doc -> {
                    BlogPost blog = doc.toObject(BlogPost.class);
                    if (blog == null) {
                        callback.onError(new IllegalStateException("Blog post not found"));
                    } else {
                        callback.onSuccess(blog);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void createBlog(BlogPost blog, RepoCallback<Void> callback) {
        var docRef = firestore.collection(FirestoreCollections.BLOGS).document();
        blog.setId(docRef.getId());
        docRef.set(blog)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void deleteBlog(String blogId, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.BLOGS).document(blogId).delete()
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }
}
