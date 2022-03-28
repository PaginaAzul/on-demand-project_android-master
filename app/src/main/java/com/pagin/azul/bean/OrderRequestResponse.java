package com.pagin.azul.bean;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderRequestResponse implements Serializable {

    private final static long serialVersionUID = -6752337695285944011L;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("location1")
    @Expose
    private OrderLocationOneResponse location1;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dropOffCoordinates")
    @Expose
    private List<Object> dropOffCoordinates = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("createdAt1")
    @Expose
    private String createdAt1;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("pickupLocation")
    @Expose
    private String pickupLocation;
    @SerializedName("pickupLat")
    @Expose
    private Integer pickupLat;
    @SerializedName("pickupLong")
    @Expose
    private Float pickupLong;
    @SerializedName("dropOffLocation")
    @Expose
    private String dropOffLocation;
    @SerializedName("dropOffLat")
    @Expose
    private Float dropOffLat;
    @SerializedName("dropOffLong")
    @Expose
    private Float dropOffLong;
    @SerializedName("termsAndCondition")
    @Expose
    private Boolean termsAndCondition;
    @SerializedName("seletTime")
    @Expose
    private String seletTime;
    @SerializedName("orderDetails")
    @Expose
    private String orderDetails;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("deliveryActiveOrder")
    @Expose
    private String deliveryActiveOrder;
    @SerializedName("professionalActiveOrder")
    @Expose
    private String professionalActiveOrder;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getDeliveryActiveOrder() {
        return deliveryActiveOrder;
    }

    public void setDeliveryActiveOrder(String deliveryActiveOrder) {
        this.deliveryActiveOrder = deliveryActiveOrder;
    }

    public String getProfessionalActiveOrder() {
        return professionalActiveOrder;
    }

    public void setProfessionalActiveOrder(String professionalActiveOrder) {
        this.professionalActiveOrder = professionalActiveOrder;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OrderLocationOneResponse getLocation1() {
        return location1;
    }

    public void setLocation1(OrderLocationOneResponse location1) {
        this.location1 = location1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getDropOffCoordinates() {
        return dropOffCoordinates;
    }

    public void setDropOffCoordinates(List<Object> dropOffCoordinates) {
        this.dropOffCoordinates = dropOffCoordinates;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt1() {
        return createdAt1;
    }

    public void setCreatedAt1(String createdAt1) {
        this.createdAt1 = createdAt1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Integer getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(Integer pickupLat) {
        this.pickupLat = pickupLat;
    }

    public Float getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(Float pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public Float getDropOffLat() {
        return dropOffLat;
    }

    public void setDropOffLat(Float dropOffLat) {
        this.dropOffLat = dropOffLat;
    }

    public Float getDropOffLong() {
        return dropOffLong;
    }

    public void setDropOffLong(Float dropOffLong) {
        this.dropOffLong = dropOffLong;
    }

    public Boolean getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(Boolean termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public String getSeletTime() {
        return seletTime;
    }

    public void setSeletTime(String seletTime) {
        this.seletTime = seletTime;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}