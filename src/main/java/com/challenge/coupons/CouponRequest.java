package com.challenge.coupons;

import java.util.List;


public class CouponRequest {
    private List<String> itemIds;
    private double couponAmount;

    
    public List<String> getItemIds() {
        return itemIds;
    }
    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }
    public double getCouponAmount() {
        return couponAmount;
    }
    public void setCouponAmount(double amount) {
        this.couponAmount = amount;
    }
}