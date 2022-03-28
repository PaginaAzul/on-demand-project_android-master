package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;

public class SignUpWithMobileResponse implements Serializable {

    @SerializedName("userType")
    @Expose
    private String userType;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("signupWithDeliveryPerson")
    @Expose
    private String signupWithDeliveryPerson;
    @SerializedName("signupWithProfessionalWorker")
    @Expose
    private String signupWithProfessionalWorker;
    @SerializedName("certificateVerify")
    @Expose
    private String certificateVerify;
    @SerializedName("signupCompeted")
    @Expose
    private Boolean signupCompeted;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("normalUserNotification")
    @Expose
    private Boolean normalUserNotification;
    @SerializedName("approvalStatus")
    @Expose
    private String approvalStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("createdAt1")
    @Expose
    private String createdAt1;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("deliveryPAboutUs")
    @Expose
    private String deliveryPAboutUs;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("vehicleLicense")
    @Expose
    private String vehicleLicense;
    @SerializedName("uploadedInsurance")
    @Expose
    private String uploadedInsurance;
    @SerializedName("deliveryPEmergencyContact")
    @Expose
    private String deliveryPEmergencyContact;
    @SerializedName("deliverPId1")
    @Expose
    private String deliverPId1;
    @SerializedName("deliveryPId2")
    @Expose
    private String deliveryPId2;
    @SerializedName("deliveryPProfilePic")
    @Expose
    private String deliveryPProfilePic;
    @SerializedName("addresses")
    @Expose
    private List<Object> addresses = null;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("jwtToken")
    @Expose
    private String jwtToken;
    @SerializedName("onlineStatus")
    @Expose
    private String onlineStatus;
    @SerializedName("appLanguage")
    @Expose
    private String appLanguage;

    public String getAppLanguage() {
        return appLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSignupWithDeliveryPerson() {
        return signupWithDeliveryPerson;
    }

    public void setSignupWithDeliveryPerson(String signupWithDeliveryPerson) {
        this.signupWithDeliveryPerson = signupWithDeliveryPerson;
    }

    public String getSignupWithProfessionalWorker() {
        return signupWithProfessionalWorker;
    }

    public void setSignupWithProfessionalWorker(String signupWithProfessionalWorker) {
        this.signupWithProfessionalWorker = signupWithProfessionalWorker;
    }

    public String getCertificateVerify() {
        return certificateVerify;
    }

    public void setCertificateVerify(String certificateVerify) {
        this.certificateVerify = certificateVerify;
    }

    public Boolean getSignupCompeted() {
        return signupCompeted;
    }

    public void setSignupCompeted(Boolean signupCompeted) {
        this.signupCompeted = signupCompeted;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Boolean getNormalUserNotification() {
        return normalUserNotification;
    }

    public void setNormalUserNotification(Boolean normalUserNotification) {
        this.normalUserNotification = normalUserNotification;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDeliveryPAboutUs() {
        return deliveryPAboutUs;
    }

    public void setDeliveryPAboutUs(String deliveryPAboutUs) {
        this.deliveryPAboutUs = deliveryPAboutUs;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleLicense() {
        return vehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        this.vehicleLicense = vehicleLicense;
    }

    public String getUploadedInsurance() {
        return uploadedInsurance;
    }

    public void setUploadedInsurance(String uploadedInsurance) {
        this.uploadedInsurance = uploadedInsurance;
    }

    public String getDeliveryPEmergencyContact() {
        return deliveryPEmergencyContact;
    }

    public void setDeliveryPEmergencyContact(String deliveryPEmergencyContact) {
        this.deliveryPEmergencyContact = deliveryPEmergencyContact;
    }

    public String getDeliverPId1() {
        return deliverPId1;
    }

    public void setDeliverPId1(String deliverPId1) {
        this.deliverPId1 = deliverPId1;
    }

    public String getDeliveryPId2() {
        return deliveryPId2;
    }

    public void setDeliveryPId2(String deliveryPId2) {
        this.deliveryPId2 = deliveryPId2;
    }

    public String getDeliveryPProfilePic() {
        return deliveryPProfilePic;
    }

    public void setDeliveryPProfilePic(String deliveryPProfilePic) {
        this.deliveryPProfilePic = deliveryPProfilePic;
    }

    public List<Object> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Object> addresses) {
        this.addresses = addresses;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

}

