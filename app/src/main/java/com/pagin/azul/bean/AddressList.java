package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressList implements Serializable {

    private String _id;
    private String address;
    private String title;
    private String landmark;
    private String lat;
    private String buildingAndApart;

    boolean isSelect = false;

    @SerializedName("long")
    private String longs;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBuildingAndApart() {
        return buildingAndApart;
    }

    public void setBuildingAndApart(String buildingAndApart) {
        this.buildingAndApart = buildingAndApart;
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
