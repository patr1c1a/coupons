package com.challenge.coupons;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MercadoLibreItemResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("price")
    private double price;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}