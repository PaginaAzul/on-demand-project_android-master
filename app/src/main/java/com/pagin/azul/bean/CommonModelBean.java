package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommonModelBean {

    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String response_message;

    @SerializedName("Data")
    private ArrayList<CommonResponseBean> Data;

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

    public ArrayList<CommonResponseBean> getData() {
        return Data;
    }

    public void setData(ArrayList<CommonResponseBean> data) {
        Data = data;
    }
}
