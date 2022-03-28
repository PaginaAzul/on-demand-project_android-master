package com.pagin.azul.bean;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInResponse implements Serializable{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("response")
    @Expose
    private SignInInner response;

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

    public SignInInner getResponse() {
        return response;
    }

    public void setResponse(SignInInner response) {
        this.response = response;
    }
}
