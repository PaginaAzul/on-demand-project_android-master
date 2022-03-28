package com.pagin.azul.bean;

import java.io.Serializable;

public class UpdateSettingNoti implements Serializable {

   private String status;
   private String  response_message;
   private Boolean response;

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

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }
}
