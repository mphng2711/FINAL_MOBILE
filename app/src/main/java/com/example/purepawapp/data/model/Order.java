package com.example.purepawapp.data.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id = "";
    private String orderCode = "";
    private String userId = "";
    private List<OrderItem> items = new ArrayList<>();
    private Address shippingAddress = new Address();
    private String couponCode = "";
    private double subtotal = 0.0;
    private double discountAmount = 0.0;
    private double shippingFee = 0.0;
    private double totalAmount = 0.0;
    private String paymentMethod = "";
    private String status = "";
    private List<OrderStatusEvent> statusHistory = new ArrayList<>();
    private String note = "";
    private String cancelReason = "";
    private long createdAt = System.currentTimeMillis();

    public Order() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<OrderStatusEvent> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<OrderStatusEvent> statusHistory) { this.statusHistory = statusHistory; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
