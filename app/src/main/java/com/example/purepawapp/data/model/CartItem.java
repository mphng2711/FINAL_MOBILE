package com.example.purepawapp.data.model;

public class CartItem {
    private Product product;
    private ProductVariant variant;
    private int quantity = 1;

    public CartItem() {
    }

    public CartItem(Product product, ProductVariant variant, int quantity) {
        this.product = product;
        this.variant = variant;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public ProductVariant getVariant() { return variant; }
    public void setVariant(ProductVariant variant) { this.variant = variant; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() {
        return variant.getEffectivePrice();
    }

    public double getLineTotal() {
        return getUnitPrice() * quantity;
    }
}
