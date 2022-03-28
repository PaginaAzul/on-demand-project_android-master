package com.pagin.azul.onphasesecond.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductModel {

    @SerializedName("response_code")
    private String response_code;

    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String response_message;

    @SerializedName("Data")
    private ArrayList<ProductResponse> Data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public ArrayList<ProductResponse> getData() {
        return Data;
    }

    public void setData(ArrayList<ProductResponse> data) {
        Data = data;
    }
}
