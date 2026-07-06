package com.example.purepawapp.data.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String id = "";
    private String name = "";
    private String slug = "";
    private String categoryId = "";
    private String description = "";
    private String shortDescription = "";
    private List<String> images = new ArrayList<>();
    private String thumbnail = "";
    private List<ProductVariant> variants = new ArrayList<>();
    private String brand = "";
    private String petType = "";
    private List<String> tags = new ArrayList<>();
    private double averageRating = 0.0;
    private int totalReviews = 0;
    private int totalSold = 0;
    private boolean isActive = true;
    private boolean isFeatured = false;

    public Product() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public List<ProductVariant> getVariants() { return variants; }
    public void setVariants(List<ProductVariant> variants) { this.variants = variants; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getPetType() { return petType; }
    public void setPetType(String petType) { this.petType = petType; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }

    public int getTotalSold() { return totalSold; }
    public void setTotalSold(int totalSold) { this.totalSold = totalSold; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public ProductVariant getDefaultVariant() {
        return variants.isEmpty() ? null : variants.get(0);
    }

    public double getDisplayPrice() {
        ProductVariant v = getDefaultVariant();
        return v != null ? v.getEffectivePrice() : 0.0;
    }

    public String getDisplayImage() {
        if (thumbnail != null && !thumbnail.isBlank()) return thumbnail;
        return images.isEmpty() ? "" : images.get(0);
    }
}
