package com.pagin.azul.onphasesecond.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyOrderResponse {

    @SerializedName("_id")
    private String _id;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("status")
    private String status;

    @SerializedName("resAndStoreId")
    private String resAndStoreId;

    @SerializedName("deliveryCharge")
    private String deliveryCharge;

    @SerializedName("totalPrice")
    private String totalPrice;

    @SerializedName("price")
    private String price;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("orderNumber")
    private String orderNumber;

    @SerializedName("orderData")
    private ArrayList<MyOrderResponse> orderData;

    @SerializedName("productData")
    private MyOrderResponse productData;

    @SerializedName("actualAmount")
    private String actualAmount;

    @SerializedName("amountWithQuantuty")
    private String amountWithQuantuty;

    @SerializedName("productId")
    private String productId;

    @SerializedName("sellerData")
    private MyOrderResponse sellerData;

    @SerializedName("driverData")
    private MyOrderResponse driverData;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("deliveryDate")
    private String deliveryDate;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("excepetdDeliveryTime")
    private String excepetdDeliveryTime;

    @SerializedName("productImage")
    private String productImage;

    @SerializedName("productName")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("productType")
    private String productType;

    @SerializedName("currency")
    private String currency;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("mobileNumber")
    private String mobileNumber;

    @SerializedName("orderType")
    private String orderType;

    @SerializedName("deliveryTimeSlot")
    private String deliveryTimeSlot;

    @SerializedName("offerPrice")
    private String offerPrice;

    @SerializedName("ratingData")
    private MyOrderResponse ratingData;

    @SerializedName("cancelStatus")
    private boolean cancelStatus;

    public MyOrderResponse getRatingData() {
        return ratingData;
    }

    public void setRatingData(MyOrderResponse ratingData) {
        this.ratingData = ratingData;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public boolean isCancelStatus() {
        return cancelStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }

    public void setDeliveryTimeSlot(String deliveryTimeSlot) {
        this.deliveryTimeSlot = deliveryTimeSlot;
    }

    public boolean getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public MyOrderResponse getDriverData() {
        return driverData;
    }

    public void setDriverData(MyOrderResponse driverData) {
        this.driverData = driverData;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public MyOrderResponse getProductData() {
        return productData;
    }

    public void setProductData(MyOrderResponse productData) {
        this.productData = productData;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExcepetdDeliveryTime() {
        return excepetdDeliveryTime;
    }

    public void setExcepetdDeliveryTime(String excepetdDeliveryTime) {
        this.excepetdDeliveryTime = excepetdDeliveryTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResAndStoreId() {
        return resAndStoreId;
    }

    public void setResAndStoreId(String resAndStoreId) {
        this.resAndStoreId = resAndStoreId;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ArrayList<MyOrderResponse> getOrderData() {
        return orderData;
    }

    public void setOrderData(ArrayList<MyOrderResponse> orderData) {
        this.orderData = orderData;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getAmountWithQuantuty() {
        return amountWithQuantuty;
    }

    public void setAmountWithQuantuty(String amountWithQuantuty) {
        this.amountWithQuantuty = amountWithQuantuty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public MyOrderResponse getSellerData() {
        return sellerData;
    }

    public void setSellerData(MyOrderResponse sellerData) {
        this.sellerData = sellerData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
