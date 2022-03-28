package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class OrderDetailInner {

    @SerializedName("pickupLocation")
    private String pickupLocation;

    @SerializedName("dropOffLocation")
    private String dropOffLocation;

    @SerializedName("seletTime")
    private String seletTime;

    @SerializedName("orderDetails")
    private String orderDetails;

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public String getSeletTime() {
        return seletTime;
    }

    public void setSeletTime(String seletTime) {
        this.seletTime = seletTime;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }
}
