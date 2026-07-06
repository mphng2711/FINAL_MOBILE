package com.example.purepawapp.data.model;

public class OrderStatusEvent {
    private String status = "";
    private long timestamp = System.currentTimeMillis();

    public OrderStatusEvent() {
    }

    public OrderStatusEvent(String status, long timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
