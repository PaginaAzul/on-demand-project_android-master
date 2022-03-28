package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class RequestOrderResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("Data")
    private RequestOrderInner requestOrderInner;

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

    public RequestOrderInner getRequestOrderInner() {
        return requestOrderInner;
    }

    public void setRequestOrderInner(RequestOrderInner requestOrderInner) {
        this.requestOrderInner = requestOrderInner;
    }
}
