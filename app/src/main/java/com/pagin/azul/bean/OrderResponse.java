package com.pagin.azul.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName(value = "response_message",alternate = "message")
    @Expose
    private String responseMessage;
    @SerializedName("Data")
    @Expose
    private OrderRequestResponse data;
    private final static long serialVersionUID = -2865399533558646078L;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public OrderRequestResponse getData() {
        return data;
    }

    public void setData(OrderRequestResponse data) {
        this.data = data;
    }

}
