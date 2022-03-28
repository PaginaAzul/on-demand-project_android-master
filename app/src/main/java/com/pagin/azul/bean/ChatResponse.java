package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatResponse {


    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("Data1")
    private ArrayList<MessageChat> dataList;

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

    public ArrayList<MessageChat> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<MessageChat> dataList) {
        this.dataList = dataList;
    }
}
