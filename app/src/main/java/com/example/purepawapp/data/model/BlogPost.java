package com.example.purepawapp.data.model;

public class BlogPost {
    private String id = "";
    private String title = "";
    private String coverImage = "";
    private String contentHtml = "";
    private String author = "";
    private String status = "published";
    private long publishedAt = System.currentTimeMillis();

    public BlogPost() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public String getContentHtml() { return contentHtml; }
    public void setContentHtml(String contentHtml) { this.contentHtml = contentHtml; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getPublishedAt() { return publishedAt; }
    public void setPublishedAt(long publishedAt) { this.publishedAt = publishedAt; }
}
