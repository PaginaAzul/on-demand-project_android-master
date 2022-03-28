package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class DeliveryPersonInner {
    @SerializedName("_id")
    private String _id;

    @SerializedName("deliveryPAboutUs")
    private String deliveryPAboutUs;

    @SerializedName("vehicleType")
    private String vehicleType;

    @SerializedName("vehicleNumber")
    private String vehicleNumber;

    @SerializedName("vehicleLicense")
    private String vehicleLicense;

    @SerializedName("insuranceNumber")
    private String insuranceNumber;

    @SerializedName("uploadedInsurance")
    private String uploadedInsurance;

    @SerializedName("deliveryPBankAC")
    private String deliveryPBankAC;

    @SerializedName("deliveryPEmergencyContact")
    private String deliveryPEmergencyContact;

    @SerializedName("deliverPId1")
    private String deliverPId1;

    @SerializedName("deliveryPId2")
    private String deliveryPId2;

    @SerializedName("deliveryPProfilePic")
    private String deliveryPProfilePic;

    @SerializedName("deliveryPersonUniqueId")
    private String deliveryPersonUniqueId;

    public String getDeliveryPersonUniqueId() {
        return deliveryPersonUniqueId;
    }

    public void setDeliveryPersonUniqueId(String deliveryPersonUniqueId) {
        this.deliveryPersonUniqueId = deliveryPersonUniqueId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getUploadedInsurance() {
        return uploadedInsurance;
    }

    public void setUploadedInsurance(String uploadedInsurance) {
        this.uploadedInsurance = uploadedInsurance;
    }

    public String getDeliveryPBankAC() {
        return deliveryPBankAC;
    }

    public void setDeliveryPBankAC(String deliveryPBankAC) {
        this.deliveryPBankAC = deliveryPBankAC;
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

}
