package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class UserTypeResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("response_message")
    private String message;
  @SerializedName("response")
    private UserTypeResponse UserTypeResponse;

    @SerializedName("Data")
    private String typeData;

    @SerializedName("adminVerifyDeliveryPerson")
    private String adminVerifyDeliveryPerson;
    @SerializedName("signupWithNormalPerson")
    private String signupWithNormalPerson;

    @SerializedName("adminVerifyProfessionalWorker")
    private String adminVerifyProfessionalWorker;
    @SerializedName("signupWithProfessionalWorker")
    private String signupWithProfessionalWorker;
    @SerializedName("signupWithDeliveryPerson")
    private String signupWithDeliveryPerson;

    @SerializedName("profilePic")
    private String profilePic;

    @SerializedName("name")
    private String name;

    @SerializedName("serviceType")
    private String serviceType;

    public String getName() {
        return name;
    }

    public com.pagin.azul.bean.UserTypeResponse getUserTypeResponse() {
        return UserTypeResponse;
    }

    public void setUserTypeResponse(com.pagin.azul.bean.UserTypeResponse userTypeResponse) {
        UserTypeResponse = userTypeResponse;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSignupWithProfessionalWorker() {
        return signupWithProfessionalWorker;
    }

    public void setSignupWithProfessionalWorker(String signupWithProfessionalWorker) {
        this.signupWithProfessionalWorker = signupWithProfessionalWorker;
    }

    public String getSignupWithDeliveryPerson() {
        return signupWithDeliveryPerson;
    }

    public void setSignupWithDeliveryPerson(String signupWithDeliveryPerson) {
        this.signupWithDeliveryPerson = signupWithDeliveryPerson;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getAdminVerifyDeliveryPerson() {
        return adminVerifyDeliveryPerson;
    }

    public void setAdminVerifyDeliveryPerson(String adminVerifyDeliveryPerson) {
        this.adminVerifyDeliveryPerson = adminVerifyDeliveryPerson;
    }

    public String getSignupWithNormalPerson() {
        return signupWithNormalPerson;
    }

    public void setSignupWithNormalPerson(String signupWithNormalPerson) {
        this.signupWithNormalPerson = signupWithNormalPerson;
    }

    public String getAdminVerifyProfessionalWorker() {
        return adminVerifyProfessionalWorker;
    }

    public void setAdminVerifyProfessionalWorker(String adminVerifyProfessionalWorker) {
        this.adminVerifyProfessionalWorker = adminVerifyProfessionalWorker;
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

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }
}
