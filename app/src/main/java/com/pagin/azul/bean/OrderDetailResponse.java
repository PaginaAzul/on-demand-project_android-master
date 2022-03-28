package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("response")
    private OrderDetailInner detailInner;


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

    public OrderDetailInner getDetailInner() {
        return detailInner;
    }

    public void setDetailInner(OrderDetailInner detailInner) {
        this.detailInner = detailInner;
    }
}
