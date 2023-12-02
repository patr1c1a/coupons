package com.challenge.coupons;

import java.util.List;


public class CouponResponse {
    private List<String> itemIds;
    private double totalExpenditure;
    
    public CouponResponse(List<String> itemIds, double totalExpenditure) {
        this.itemIds=itemIds;
        this.totalExpenditure=totalExpenditure;
    }
    
    public CouponResponse() {
    }
    
    public List<String> getItemIds() {
        return itemIds;
    }
    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }
    public double getTotalExpenditure() {
        return totalExpenditure;
    }
    public void setTotalExpenditure(double total) {
        this.totalExpenditure = total;
    }
}
