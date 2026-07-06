package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.BlogPost
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface BlogRepository {
    suspend fun getBlogs(): Result<List<BlogPost>>
    suspend fun getBlog(blogId: String): Result<BlogPost>
    suspend fun createBlog(blog: BlogPost): Result<Unit>
    suspend fun deleteBlog(blogId: String): Result<Unit>
}

class BlogRepositoryImpl(
    private val firestore: FirebaseFirestore
) : BlogRepository {

    override suspend fun getBlogs(): Result<List<BlogPost>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.BLOGS)
            .orderBy("publishedAt")
            .get()
            .await()
        snapshot.toObjects(BlogPost::class.java).sortedByDescending { it.publishedAt }
    }

    override suspend fun getBlog(blogId: String): Result<BlogPost> = runCatching {
        val doc = firestore.collection(FirestoreCollections.BLOGS).document(blogId).get().await()
        doc.toObject(BlogPost::class.java) ?: error("Blog post not found")
    }

    override suspend fun createBlog(blog: BlogPost): Result<Unit> = runCatching {
        val docRef = firestore.collection(FirestoreCollections.BLOGS).document()
        docRef.set(blog.copy(id = docRef.id)).await()
        Unit
    }

    override suspend fun deleteBlog(blogId: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.BLOGS).document(blogId).delete().await()
        Unit
    }
}
