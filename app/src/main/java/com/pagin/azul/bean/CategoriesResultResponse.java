package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class CategoriesResultResponse {

    @SerializedName("results")
    ArrayList<CategoriesResultInner> results;
    @SerializedName("status")
    private String status;

    public ArrayList<CategoriesResultInner> getResults() {
        return results;
    }

    public void setResults(ArrayList<CategoriesResultInner> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
