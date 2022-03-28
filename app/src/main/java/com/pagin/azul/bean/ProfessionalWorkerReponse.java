package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class ProfessionalWorkerReponse {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;

    @SerializedName("Data")
    private ProfessionalWorkerInner professionalWorkerInner;

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

    public ProfessionalWorkerInner getProfessionalWorkerInner() {
        return professionalWorkerInner;
    }

    public void setProfessionalWorkerInner(ProfessionalWorkerInner professionalWorkerInner) {
        this.professionalWorkerInner = professionalWorkerInner;
    }
}
