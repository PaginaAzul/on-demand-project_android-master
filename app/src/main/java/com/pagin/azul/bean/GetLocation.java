package com.pagin.azul.bean;

import java.io.Serializable;

public class GetLocation implements Serializable {

    private String status;
    private String response_message;
    private String Data;
    private Obj1 obj1;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

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

    public Obj1 getObj1() {
        return obj1;
    }

    public void setObj1(Obj1 obj1) {
        this.obj1 = obj1;
    }
}
