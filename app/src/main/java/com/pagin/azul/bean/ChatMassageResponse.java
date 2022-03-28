package com.pagin.azul.bean;

public class ChatMassageResponse {

    private String nickname;
    private String message ;
    private String sendderId;
    private String creayedTime;
    private String recieverId;

    public ChatMassageResponse(String nickname, String message, String sendderId, String creayedTime, String recieverId) {
        this.nickname = nickname;
        this.message = message;
        this.sendderId = sendderId;
        this.creayedTime = creayedTime;
        this.recieverId = recieverId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendderId() {
        return sendderId;
    }

    public void setSendderId(String sendderId) {
        this.sendderId = sendderId;
    }

    public String getCreayedTime() {
        return creayedTime;
    }

    public void setCreayedTime(String creayedTime) {
        this.creayedTime = creayedTime;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }
}
