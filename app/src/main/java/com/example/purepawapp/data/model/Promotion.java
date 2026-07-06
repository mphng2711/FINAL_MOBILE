package com.example.purepawapp.data.model;

public class Promotion {
    private String code = "";
    private String description = "";
    private String discountType = "";
    private double discountValue = 0.0;
    private double minOrderAmount = 0.0;
    private double maxDiscountAmount = 0.0;
    private int usageLimit = 0;
    private int usedCount = 0;
    private int usageLimitPerUser = 1;
    private String applicableTo = "all";
    private long startDate = 0L;
    private long endDate = 0L;
    private boolean isActive = true;

    public Promotion() {
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

    public double getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(double minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public double getMaxDiscountAmount() { return maxDiscountAmount; }
    public void setMaxDiscountAmount(double maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; }

    public int getUsageLimit() { return usageLimit; }
    public void setUsageLimit(int usageLimit) { this.usageLimit = usageLimit; }

    public int getUsedCount() { return usedCount; }
    public void setUsedCount(int usedCount) { this.usedCount = usedCount; }

    public int getUsageLimitPerUser() { return usageLimitPerUser; }
    public void setUsageLimitPerUser(int usageLimitPerUser) { this.usageLimitPerUser = usageLimitPerUser; }

    public String getApplicableTo() { return applicableTo; }
    public void setApplicableTo(String applicableTo) { this.applicableTo = applicableTo; }

    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
