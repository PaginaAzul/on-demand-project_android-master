package com.pagin.azul.bean;

import java.io.Serializable;
import java.util.List;

public class AddressApi implements Serializable {

    private String status;
    private String response_message;
    private List<DataList> Data;

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

    public List<DataList> getData() {
        return Data;
    }

    public void setData(List<DataList> data) {
        Data = data;
    }
}
