package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class CommonModel {

    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String response_message;

    @SerializedName("Data")
    private CommonResponse[] Data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public CommonResponse[] getData() {
        return Data;
    }

    public void setData(CommonResponse[] data) {
        Data = data;
    }
}
