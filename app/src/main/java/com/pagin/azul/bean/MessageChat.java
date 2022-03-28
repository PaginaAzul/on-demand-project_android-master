package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class MessageChat {

    @SerializedName("senderId")
    private String senderId;

    @SerializedName("receiverId")
    private String receiverId;

    @SerializedName("message")
    private String message;

    @SerializedName("messageType")
    private String messageType;

    @SerializedName("media")
    private String media;

    @SerializedName("url")
    private String url;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("from")
    private String from;

    @SerializedName("profilePic")
    private String profilePic;

    @SerializedName("locationType")
    private String locationType;


    public MessageChat(String senderId, String receiverId, String message, String messageType, String media, String createdAt, String profilePic,String url) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.messageType = messageType;
        this.media = media;
        this.createdAt = createdAt;
        this.profilePic = profilePic;
        this.url = url;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
