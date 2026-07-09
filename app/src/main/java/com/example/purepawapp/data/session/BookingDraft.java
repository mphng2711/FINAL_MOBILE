package com.example.purepawapp.data.session;

import com.example.purepawapp.data.model.Pet;

public class BookingDraft {

    private String serviceId = "";
    private String serviceName = "";
    private String serviceType = "";
    private double price = 0.0;

    private String bookingDate = "";
    private String bookingDateLabel = "";
    private String timeSlot = "";

    private Pet pet;

    private String paymentMethodCode = "store";
    private String paymentMethodLabel = "Thanh toán tại cửa hàng";

    public String getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public String getServiceType() { return serviceType; }
    public double getPrice() { return price; }

    public void setService(String serviceId, String serviceName, String serviceType, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.price = price;
    }

    public String getBookingDate() { return bookingDate; }
    public String getBookingDateLabel() { return bookingDateLabel; }
    public String getTimeSlot() { return timeSlot; }

    public void setDateTime(String bookingDate, String bookingDateLabel, String timeSlot) {
        this.bookingDate = bookingDate;
        this.bookingDateLabel = bookingDateLabel;
        this.timeSlot = timeSlot;
    }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }

    public String getPaymentMethodCode() { return paymentMethodCode; }
    public String getPaymentMethodLabel() { return paymentMethodLabel; }

    public void setPaymentMethod(String code, String label) {
        this.paymentMethodCode = code;
        this.paymentMethodLabel = label;
    }

    public void reset() {
        serviceId = "";
        serviceName = "";
        serviceType = "";
        price = 0.0;
        bookingDate = "";
        bookingDateLabel = "";
        timeSlot = "";
        pet = null;
        paymentMethodCode = "store";
        paymentMethodLabel = "Thanh toán tại cửa hàng";
    }
}
