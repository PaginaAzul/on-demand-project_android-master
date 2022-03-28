package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class ProfessionalWorkerResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
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
