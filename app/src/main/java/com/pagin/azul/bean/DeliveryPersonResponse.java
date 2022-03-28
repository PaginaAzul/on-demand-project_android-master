package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class DeliveryPersonResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("Data")
    private DeliveryPersonInner deliveryPersonInner;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DeliveryPersonInner getDeliveryPersonInner() {
        return deliveryPersonInner;
    }

    public void setDeliveryPersonInner(DeliveryPersonInner deliveryPersonInner) {
        this.deliveryPersonInner = deliveryPersonInner;
    }
}
