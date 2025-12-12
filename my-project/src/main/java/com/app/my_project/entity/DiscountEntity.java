package com.app.my_project.entity;

public class DiscountEntity {

    private String couponName;
    private Double couponAmount;
    private String onTopName;
    private Double onTopAmount;
    private String onTopCategory;
    private String seasonalName;
    private Long seasonalCap;
    private Double seasonalAmount;

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getOnTopName() {
        return onTopName;
    }

    public void setOnTopName(String onTopName) {
        this.onTopName = onTopName;
    }

    public Double getOnTopAmount() {
        return onTopAmount;
    }

    public void setOnTopAmount(Double onTopAmount) {
        this.onTopAmount = onTopAmount;
    }

    public String getOnTopCategory() {
        return onTopCategory;
    }

    public void setOnTopCategory(String onTopCategory) {
        this.onTopCategory = onTopCategory;
    }

    public String getSeasonalName() {
        return seasonalName;
    }

    public void setSeasonalName(String seasonalName) {
        this.seasonalName = seasonalName;
    }

    public Long getSeasonalCap() {
        return seasonalCap;
    }

    public void setSeasonalCap(Long seasonalCap) {
        this.seasonalCap = seasonalCap;
    }

    public Double getSeasonalAmount() {
        return seasonalAmount;
    }

    public void setSeasonalAmount(Double seasonalAmount) {
        this.seasonalAmount = seasonalAmount;
    }

}
