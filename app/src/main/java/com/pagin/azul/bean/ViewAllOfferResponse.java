package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class ViewAllOfferResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("Data")
    private ViewAllOfferInner allOfferInner;

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

    public ViewAllOfferInner getAllOfferInner() {
        return allOfferInner;
    }

    public void setAllOfferInner(ViewAllOfferInner allOfferInner) {
        this.allOfferInner = allOfferInner;
    }
}
