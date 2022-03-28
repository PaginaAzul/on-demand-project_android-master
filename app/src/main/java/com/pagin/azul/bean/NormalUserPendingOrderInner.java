package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NormalUserPendingOrderInner implements Serializable {
    @SerializedName("docs")
    private ArrayList<NormalUserPendingOrderInner> OrderdocData;
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
    @SerializedName("notiMessage")
    private String notiMessage;
    @SerializedName("roomId")
    private String roomId;

    @SerializedName("apprxTime")
    private String apprxTime;
    @SerializedName("pickupLat")
    private String pickupLat;
    @SerializedName("pickupLong")
    private String pickupLong;
    @SerializedName("dropOffLat")
    private String dropOffLat;
    @SerializedName("dropOffLong")
    private String dropOffLong;
    @SerializedName("pickupLocation")
    private String pickupLocation;
    @SerializedName("orderDetails")
    private String orderDetails;
    @SerializedName("ratingByName")
    private String ratingByName;
    @SerializedName("comments")
    private String comments;
    @SerializedName("orderNumber")
    private String orderNumber;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("TotalOffer")
    private String TotalOffer;
    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("offerAcceptedOfId")
    private String offerAcceptedOfId;
    @SerializedName("offerAcceptedByName")
    private String offerAcceptedByName;
    @SerializedName("offerAcceptedById")
    private String offerAcceptedById;
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
    @SerializedName("TotalRating")
    private String TotalRating;
    @SerializedName("AvgRating")
    private String AvgRating;
    @SerializedName("transportMode")
    private String transportMode;
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
    @SerializedName("invoiceStatus")
    private String invoiceStatus;
    @SerializedName("goStatus")
    private String goStatus;
    @SerializedName("invoicePdf")
    private String invoicePdf;
    @SerializedName("offerMakeByName")
    private String offerMakeByName;
    @SerializedName("makeOfferById")
    private String makeOfferById;
    @SerializedName("currentToDrLocation")
    private String currentToDrLocation;
    @SerializedName("selectSubSubCategoryName")
    private String selectSubSubCategoryName;
    @SerializedName("selectSubCategoryName")
    private String selectSubCategoryName;
    @SerializedName("orderOwner")
    private String orderOwner;
    @SerializedName("ratingBy1")
    private String ratingBy1;
    @SerializedName("serviceType")
    private String serviceType;
    @SerializedName("offerAcceptedOfProfilePic")
    private String offerAcceptedOfProfilePic;
    @SerializedName("profilePic")
    private String profilePic;
    @SerializedName("realOrderId")
    private String realOrderId;
    @SerializedName("offerId")
    private String offerId;
 @SerializedName("popupStatus")
    private String popupStatus;

    @SerializedName("invoiceSubtoatl")
    private String invoiceSubtoatl;
    @SerializedName("invoiceTax")
    private String invoiceTax;
    @SerializedName("invoiceTotal")
    private String invoiceTotal;
    @SerializedName("arrivedStatus")
    private String arrivedStatus;
    @SerializedName("rate")
    private int rate;
    @SerializedName("offerAcceptedOfMobileNumber")
    private String offerAcceptedOfMobileNumber;
    @SerializedName("offerAcceptedOfCountryCode")
    private String offerAcceptedOfCountryCode;
    @SerializedName("selectCategoryName")
    private String selectCategoryName;
    @SerializedName("portugueseCategoryName")
    private String portugueseCategoryName;
    @SerializedName("portugueseSubCategoryName")
    private String portugueseSubCategoryName;
    @SerializedName("ratingData")
    private ArrayList<NormalUserPendingOrderInner> ratingData;
    @SerializedName("workDoneStatus")
    private String workDoneStatus;
    @SerializedName("currency")
    private String currency;
    @SerializedName("locationProvider")
    private NormalUserPendingOrderInner locationProvider;
    @SerializedName("workImage")
    private ArrayList<String> workImage;
    @SerializedName("coordinates")
    private ArrayList<Double> coordinates;

    public String getPortugueseCategoryName() {
        return portugueseCategoryName;
    }

    public void setPortugueseCategoryName(String portugueseCategoryName) {
        this.portugueseCategoryName = portugueseCategoryName;
    }

    public String getPortugueseSubCategoryName() {
        return portugueseSubCategoryName;
    }

    public void setPortugueseSubCategoryName(String portugueseSubCategoryName) {
        this.portugueseSubCategoryName = portugueseSubCategoryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public NormalUserPendingOrderInner getLocationProvider() {
        return locationProvider;
    }

    public void setLocationProvider(NormalUserPendingOrderInner locationProvider) {
        this.locationProvider = locationProvider;
    }

    public ArrayList<String> getWorkImage() {
        return workImage;
    }

    public void setWorkImage(ArrayList<String> workImage) {
        this.workImage = workImage;
    }

    public String getSelectCategoryName() {
        return selectCategoryName;
    }

    public String getOfferAcceptedOfCountryCode() {
        return offerAcceptedOfCountryCode;
    }

    public void setOfferAcceptedOfCountryCode(String offerAcceptedOfCountryCode) {
        this.offerAcceptedOfCountryCode = offerAcceptedOfCountryCode;
    }

    public String getWorkDoneStatus() {
        return workDoneStatus;
    }

    public void setWorkDoneStatus(String workDoneStatus) {
        this.workDoneStatus = workDoneStatus;
    }

    public String getPopupStatus() {
        return popupStatus;
    }

    public void setPopupStatus(String popupStatus) {
        this.popupStatus = popupStatus;
    }

    public String getInvoiceSubtoatl() {
        return invoiceSubtoatl;
    }

    public void setInvoiceSubtoatl(String invoiceSubtoatl) {
        this.invoiceSubtoatl = invoiceSubtoatl;
    }

    public String getInvoiceTax() {
        return invoiceTax;
    }

    public void setInvoiceTax(String invoiceTax) {
        this.invoiceTax = invoiceTax;
    }

    public String getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(String invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }


    public String getOfferAcceptedOfMobileNumber() {
        return offerAcceptedOfMobileNumber;
    }

    public void setOfferAcceptedOfMobileNumber(String offerAcceptedOfMobileNumber) {
        this.offerAcceptedOfMobileNumber = offerAcceptedOfMobileNumber;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getNotiMessage() {
        return notiMessage;
    }

    public void setNotiMessage(String notiMessage) {
        this.notiMessage = notiMessage;
    }

    public String getArrivedStatus() {
        return arrivedStatus;
    }

    public void setArrivedStatus(String arrivedStatus) {
        this.arrivedStatus = arrivedStatus;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public ArrayList<NormalUserPendingOrderInner> getRatingData() {
        return ratingData;
    }

    public void setRatingData(ArrayList<NormalUserPendingOrderInner> ratingData) {
        this.ratingData = ratingData;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getRealOrderId() {
        return realOrderId;
    }

    public void setRealOrderId(String realOrderId) {
        this.realOrderId = realOrderId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getOfferAcceptedOfProfilePic() {
        return offerAcceptedOfProfilePic;
    }

    public void setOfferAcceptedOfProfilePic(String offerAcceptedOfProfilePic) {
        this.offerAcceptedOfProfilePic = offerAcceptedOfProfilePic;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getRatingBy1() {
        return ratingBy1;
    }

    public void setRatingBy1(String ratingBy1) {
        this.ratingBy1 = ratingBy1;
    }

    public String getOrderOwner() {
        return orderOwner;
    }

    public void setOrderOwner(String orderOwner) {
        this.orderOwner = orderOwner;
    }

    public String getSelectSubSubCategoryName() {
        return selectSubSubCategoryName;
    }

    public void setSelectSubSubCategoryName(String selectSubSubCategoryName) {
        this.selectSubSubCategoryName = selectSubSubCategoryName;
    }

    public String getSelectSubCategoryName() {
        return selectSubCategoryName;
    }

    public void setSelectSubCategoryName(String selectSubCategoryName) {
        this.selectSubCategoryName = selectSubCategoryName;
    }

    public String getCurrentToDrLocation() {
        return currentToDrLocation;
    }

    public void setCurrentToDrLocation(String currentToDrLocation) {
        this.currentToDrLocation = currentToDrLocation;
    }

    public String getMakeOfferById() {
        return makeOfferById;
    }

    public void setMakeOfferById(String makeOfferById) {
        this.makeOfferById = makeOfferById;
    }

    public String getOfferMakeByName() {
        return offerMakeByName;
    }

    public void setOfferMakeByName(String offerMakeByName) {
        this.offerMakeByName = offerMakeByName;
    }

    public String getInvoicePdf() {
        return invoicePdf;
    }

    public void setInvoicePdf(String invoicePdf) {
        this.invoicePdf = invoicePdf;
    }

    public String getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(String goStatus) {
        this.goStatus = goStatus;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getDropOffLat() {
        return dropOffLat;
    }

    public void setDropOffLat(String dropOffLat) {
        this.dropOffLat = dropOffLat;
    }

    public String getDropOffLong() {
        return dropOffLong;
    }

    public void setDropOffLong(String dropOffLong) {
        this.dropOffLong = dropOffLong;
    }

    public String getOfferAcceptedById() {
        return offerAcceptedById;
    }

    public void setOfferAcceptedById(String offerAcceptedById) {
        this.offerAcceptedById = offerAcceptedById;
    }

    public String getRatingByName() {
        return ratingByName;
    }

    public void setRatingByName(String ratingByName) {
        this.ratingByName = ratingByName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(String totalRating) {
        TotalRating = totalRating;
    }

    public String getOfferAcceptedOfId() {
        return offerAcceptedOfId;
    }

    public void setOfferAcceptedOfId(String offerAcceptedOfId) {
        this.offerAcceptedOfId = offerAcceptedOfId;
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

    public String getOfferAcceptedOfName() {
        return offerAcceptedOfName;
    }

    public void setOfferAcceptedOfName(String offerAcceptedOfName) {
        this.offerAcceptedOfName = offerAcceptedOfName;
    }

    public String getOfferAcceptedByName() {
        return offerAcceptedByName;
    }

    public void setOfferAcceptedByName(String offerAcceptedByName) {
        this.offerAcceptedByName = offerAcceptedByName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSeletTime() {
        return seletTime;
    }

    public void setSeletTime(String seletTime) {
        this.seletTime = seletTime;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<NormalUserPendingOrderInner> getOrderdocData() {
        return OrderdocData;
    }

    public void setOrderdocData(ArrayList<NormalUserPendingOrderInner> orderdocData) {
        OrderdocData = orderdocData;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
