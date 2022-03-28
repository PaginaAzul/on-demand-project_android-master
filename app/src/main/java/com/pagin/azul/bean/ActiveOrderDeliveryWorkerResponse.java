package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class ActiveOrderDeliveryWorkerResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("Data")
    private ActiveOrderDeliveryWorkerInner activeOrderInner;

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

    public ActiveOrderDeliveryWorkerInner getActiveOrderInner() {
        return activeOrderInner;
    }

    public void setActiveOrderInner(ActiveOrderDeliveryWorkerInner activeOrderInner) {
        this.activeOrderInner = activeOrderInner;
    }
}
