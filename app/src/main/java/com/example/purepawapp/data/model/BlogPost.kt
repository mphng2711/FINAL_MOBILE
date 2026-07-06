package com.example.purepawapp.data.model

data class BlogPost(
    val id: String = "",
    val title: String = "",
    val coverImage: String = "",
    val contentHtml: String = "",
    val author: String = "",
    val status: String = "published",
    val publishedAt: Long = System.currentTimeMillis()
)
