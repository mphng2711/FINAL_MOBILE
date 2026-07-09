package com.example.purepawapp.data.session;

import com.example.purepawapp.data.model.Address;

public class CheckoutDraft {

    private Address address;
    private String paymentMethodCode = "cod";
    private String paymentMethodLabel = "Thanh toán khi nhận hàng (COD)";

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public String getPaymentMethodLabel() {
        return paymentMethodLabel;
    }

    public void setPaymentMethod(String code, String label) {
        this.paymentMethodCode = code;
        this.paymentMethodLabel = label;
    }

    public void reset() {
        address = null;
        paymentMethodCode = "cod";
        paymentMethodLabel = "Thanh toán khi nhận hàng (COD)";
    }
}
