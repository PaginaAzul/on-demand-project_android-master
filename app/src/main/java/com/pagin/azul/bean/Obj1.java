package com.pagin.azul.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Obj1 implements Serializable {

    private String TotalRecord;
    private ArrayList<Respose> response;

    public String getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(String totalRecord) {
        TotalRecord = totalRecord;
    }

    public ArrayList<Respose> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Respose> response) {
        this.response = response;
    }
}
