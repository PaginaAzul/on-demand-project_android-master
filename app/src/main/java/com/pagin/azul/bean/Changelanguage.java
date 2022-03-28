package com.pagin.azul.bean;

import java.io.Serializable;

public class Changelanguage implements Serializable {

    private String status;
    private String response_message;
    private String Data;

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

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
