package com.pagin.azul.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignUpWithMobileResp implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("response_message")
    @Expose
    private String responseMessage;

    @SerializedName("Data")
    @Expose
    private SignUpWithMobileResponse data;


    public SignUpWithMobileResp(String status, String responseMessage, SignUpWithMobileResponse data) {
        this.status = status;
        this.responseMessage = responseMessage;
        this.data = data;
    }

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

    public SignUpWithMobileResponse getData() {
        return data;
    }

    public void setData(SignUpWithMobileResponse data) {
        this.data = data;
    }
}
