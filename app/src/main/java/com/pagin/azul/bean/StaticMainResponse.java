package com.pagin.azul.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StaticMainResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("response")
    @Expose
    private List<StaticContentResponse> response = null;
    private final static long serialVersionUID = -3514535027738965561L;

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

    public List<StaticContentResponse> getResponse() {
        return response;
    }

    public void setResponse(List<StaticContentResponse> response) {
        this.response = response;
    }

}