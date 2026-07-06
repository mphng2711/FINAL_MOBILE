package com.example.purepawapp.data.model;

public class SpaService {
    private String id = "";
    private String name = "";
    private String serviceType = "";
    private String description = "";
    private int durationMinutes = 0;
    private double price = 0.0;
    private String iconUrl = "";

    public SpaService() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
}
