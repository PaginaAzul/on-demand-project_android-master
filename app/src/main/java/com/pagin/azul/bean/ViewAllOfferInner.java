package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewAllOfferInner {

    @SerializedName("docs")
    private ArrayList<ViewAllOfferInner> getViewOfferdocs;

    @SerializedName("_id")
    private String _id;
    @SerializedName("minimumOffer")
    private String minimumOffer;

    @SerializedName("apprxTime")
    private String apprxTime;

    @SerializedName("message")
    private String message;

    @SerializedName("currentToPicupLocation")
    private String currentToPicupLocation;

    @SerializedName("pickupToDropLocation")
    private String pickupToDropLocation;

    @SerializedName("offerMakeByName")
    private String offerMakeByName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<ViewAllOfferInner> getGetViewOfferdocs() {
        return getViewOfferdocs;
    }

    public void setGetViewOfferdocs(ArrayList<ViewAllOfferInner> getViewOfferdocs) {
        this.getViewOfferdocs = getViewOfferdocs;
    }

    public String getMinimumOffer() {
        return minimumOffer;
    }

    public void setMinimumOffer(String minimumOffer) {
        this.minimumOffer = minimumOffer;
    }

    public String getApprxTime() {
        return apprxTime;
    }

    public void setApprxTime(String apprxTime) {
        this.apprxTime = apprxTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentToPicupLocation() {
        return currentToPicupLocation;
    }

    public void setCurrentToPicupLocation(String currentToPicupLocation) {
        this.currentToPicupLocation = currentToPicupLocation;
    }

    public String getPickupToDropLocation() {
        return pickupToDropLocation;
    }

    public void setPickupToDropLocation(String pickupToDropLocation) {
        this.pickupToDropLocation = pickupToDropLocation;
    }

    public String getOfferMakeByName() {
        return offerMakeByName;
    }

    public void setOfferMakeByName(String offerMakeByName) {
        this.offerMakeByName = offerMakeByName;
    }
}
