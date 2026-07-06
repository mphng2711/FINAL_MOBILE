package com.example.purepawapp.data.model;

public class ProductVariant {
    private String id = "";
    private String name = "";
    private double price = 0.0;
    private Double salePrice = null;
    private String sku = "";
    private int stock = 0;

    public ProductVariant() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getEffectivePrice() {
        return salePrice != null ? salePrice : price;
    }
}
