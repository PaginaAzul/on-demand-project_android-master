package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyProfileResponse implements Serializable {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("response")
    private MyProfileInner profileInner;

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

    public MyProfileInner getProfileInner() {
        return profileInner;
    }

    public void setProfileInner(MyProfileInner profileInner) {
        this.profileInner = profileInner;
    }
}
