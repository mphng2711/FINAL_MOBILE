package com.example.purepawapp.data.model;

import java.util.Objects;

public class Address {
    private String fullName = "";
    private String phone = "";
    private String street = "";
    private String ward = "";
    private String district = "";
    private String city = "";
    private boolean isDefault = false;

    public Address() {
    }

    public Address(String fullName, String phone, String street, String ward, String district, String city, boolean isDefault) {
        this.fullName = fullName;
        this.phone = phone;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return isDefault == address.isDefault
                && Objects.equals(fullName, address.fullName)
                && Objects.equals(phone, address.phone)
                && Objects.equals(street, address.street)
                && Objects.equals(ward, address.ward)
                && Objects.equals(district, address.district)
                && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, phone, street, ward, district, city, isDefault);
    }
}
