package com.pagin.azul.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryResponse  implements Serializable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("response")
    @Expose
    private List<CategoryDataResponse> data = null;
    private final static long serialVersionUID = 3408176344597362825L;

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

    public List<CategoryDataResponse> getData() {
        return data;
    }

    public void setData(List<CategoryDataResponse> data) {
        this.data = data;
    }

}
