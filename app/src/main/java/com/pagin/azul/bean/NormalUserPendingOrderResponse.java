package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NormalUserPendingOrderResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;


    @SerializedName("Data")
    private NormalUserPendingOrderInner pendingOrderInner;

    @SerializedName("Data1")
    private ArrayList<NormalUserPendingOrderInner> dataList;


    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profilePic")
    private String profilePic;
    @SerializedName("TotalRating")
    private String TotalRating;
    @SerializedName("AvgRating")
    private String AvgRating;
    @SerializedName("Order")
    private String Order;

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public ArrayList<NormalUserPendingOrderInner> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<NormalUserPendingOrderInner> dataList) {
        this.dataList = dataList;
    }

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

    public NormalUserPendingOrderInner getPendingOrderInner() {
        return pendingOrderInner;
    }

    public void setPendingOrderInner(NormalUserPendingOrderInner pendingOrderInner) {
        this.pendingOrderInner = pendingOrderInner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(String totalRating) {
        TotalRating = totalRating;
    }

    public String getAvgRating() {
        return AvgRating;
    }

    public void setAvgRating(String avgRating) {
        AvgRating = avgRating;
    }
}
