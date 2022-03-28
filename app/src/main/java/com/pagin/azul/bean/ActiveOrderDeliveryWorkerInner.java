package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ActiveOrderDeliveryWorkerInner implements Serializable{

    @SerializedName("docs")
    private ArrayList<ActiveOrderDeliveryWorkerInner> OrderdocData;

    @SerializedName("_id")
    private String _id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("seletTime")
    private String seletTime;

    @SerializedName("minimumOffer")
    private String minimumOffer;

    @SerializedName("message")
    private String message;

    @SerializedName("apprxTime")
    private String apprxTime;

    @SerializedName("pickupLocation")
    private String pickupLocation;

    @SerializedName("orderDetails")
    private String orderDetails;

    @SerializedName("orderNumber")
    private String orderNumber;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("TotalOffer")
    private String TotalOffer;


    @SerializedName("offerAcceptedByName")
    private String offerAcceptedByName;

    @SerializedName("dropOffLocation")
    private String dropOffLocation;

    @SerializedName("currentToPicupLocation")
    private String currentToPicupLocation;

    @SerializedName("pickupToDropLocation")
    private String pickupToDropLocation;

    @SerializedName("deliveryOffer")
    private String deliveryOffer;

    @SerializedName("tax")
    private String tax;
    @SerializedName("total")
    private String total;
    @SerializedName("invoiceCreatedAt")
    private String invoiceCreatedAt;
    @SerializedName("offerAcceptedOfName")
    private String offerAcceptedOfName;
    @SerializedName("offerAcceptedByMobileNumber")
    private String offerAcceptedByMobileNumber;
    @SerializedName("offerAcceptedByProfilePic")
    private String offerAcceptedByProfilePic;
    @SerializedName("offerAcceptedByCountryCode")
    private String offerAcceptedByCountryCode;
    @SerializedName("AvgRating")
    private String AvgRating;

    public ArrayList<ActiveOrderDeliveryWorkerInner> getOrderdocData() {
        return OrderdocData;
    }

    public void setOrderdocData(ArrayList<ActiveOrderDeliveryWorkerInner> orderdocData) {
        OrderdocData = orderdocData;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSeletTime() {
        return seletTime;
    }

    public void setSeletTime(String seletTime) {
        this.seletTime = seletTime;
    }

    public String getMinimumOffer() {
        return minimumOffer;
    }

    public void setMinimumOffer(String minimumOffer) {
        this.minimumOffer = minimumOffer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApprxTime() {
        return apprxTime;
    }

    public void setApprxTime(String apprxTime) {
        this.apprxTime = apprxTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotalOffer() {
        return TotalOffer;
    }

    public void setTotalOffer(String totalOffer) {
        TotalOffer = totalOffer;
    }

    public String getOfferAcceptedByName() {
        return offerAcceptedByName;
    }

    public void setOfferAcceptedByName(String offerAcceptedByName) {
        this.offerAcceptedByName = offerAcceptedByName;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
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

    public String getDeliveryOffer() {
        return deliveryOffer;
    }

    public void setDeliveryOffer(String deliveryOffer) {
        this.deliveryOffer = deliveryOffer;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getInvoiceCreatedAt() {
        return invoiceCreatedAt;
    }

    public void setInvoiceCreatedAt(String invoiceCreatedAt) {
        this.invoiceCreatedAt = invoiceCreatedAt;
    }

    public String getOfferAcceptedOfName() {
        return offerAcceptedOfName;
    }

    public void setOfferAcceptedOfName(String offerAcceptedOfName) {
        this.offerAcceptedOfName = offerAcceptedOfName;
    }

    public String getOfferAcceptedByMobileNumber() {
        return offerAcceptedByMobileNumber;
    }

    public void setOfferAcceptedByMobileNumber(String offerAcceptedByMobileNumber) {
        this.offerAcceptedByMobileNumber = offerAcceptedByMobileNumber;
    }

    public String getOfferAcceptedByProfilePic() {
        return offerAcceptedByProfilePic;
    }

    public void setOfferAcceptedByProfilePic(String offerAcceptedByProfilePic) {
        this.offerAcceptedByProfilePic = offerAcceptedByProfilePic;
    }

    public String getOfferAcceptedByCountryCode() {
        return offerAcceptedByCountryCode;
    }

    public void setOfferAcceptedByCountryCode(String offerAcceptedByCountryCode) {
        this.offerAcceptedByCountryCode = offerAcceptedByCountryCode;
    }

    public String getAvgRating() {
        return AvgRating;
    }

    public void setAvgRating(String avgRating) {
        AvgRating = avgRating;
    }
}
