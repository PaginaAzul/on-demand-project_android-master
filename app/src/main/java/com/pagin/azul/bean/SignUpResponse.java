package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private  String message;

    @SerializedName("response")
    private SignupInner signUpInner;


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

    public SignupInner getSignUpInner() {
        return signUpInner;
    }

    public void setSignUpInner(SignupInner signUpInner) {
        this.signUpInner = signUpInner;
    }
}
