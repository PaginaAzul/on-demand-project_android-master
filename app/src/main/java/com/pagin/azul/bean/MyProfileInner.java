package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyProfileInner implements Serializable {

    @SerializedName("userType")
    private String userType;

    @SerializedName("normalUserNotification")
    private String normalUserNotification;

    @SerializedName("status")
    private String status;

    @SerializedName("_id")
    private String _id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("userName")
    private String userName;

    @SerializedName("name")
    private String name;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dob")
    private String dob;

    @SerializedName("email")
    private String email;

    @SerializedName("countryCode")
    private String countryCode;

    @SerializedName("country")
    private String country;

    @SerializedName("appLanguage")
    private String appLanguage;

    @SerializedName("speakLanguage")
    private String speakLanguage;

    @SerializedName("mobileNumber")
    private String mobileNumber;

    @SerializedName("signupWithDeliveryPerson")
    private String signupWithDeliveryPerson;

    @SerializedName("signupWithProfessionalWorker")
    private String signupWithProfessionalWorker;

    @SerializedName("adminVerifyDeliveryPerson")
    private String adminVerifyDeliveryPerson;

    @SerializedName("adminVerifyProfessionalWorker")
    private String adminVerifyProfessionalWorker;

    @SerializedName("profilePic")
    private String profilePic;

    public String getAdminVerifyDeliveryPerson() {
        return adminVerifyDeliveryPerson;
    }

    public void setAdminVerifyDeliveryPerson(String adminVerifyDeliveryPerson) {
        this.adminVerifyDeliveryPerson = adminVerifyDeliveryPerson;
    }

    public String getAdminVerifyProfessionalWorker() {
        return adminVerifyProfessionalWorker;
    }

    public void setAdminVerifyProfessionalWorker(String adminVerifyProfessionalWorker) {
        this.adminVerifyProfessionalWorker = adminVerifyProfessionalWorker;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNormalUserNotification() {
        return normalUserNotification;
    }

    public void setNormalUserNotification(String normalUserNotification) {
        this.normalUserNotification = normalUserNotification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }

    public String getSpeakLanguage() {
        return speakLanguage;
    }

    public void setSpeakLanguage(String speakLanguage) {
        this.speakLanguage = speakLanguage;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
