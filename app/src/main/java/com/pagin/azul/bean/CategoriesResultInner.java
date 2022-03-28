package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoriesResultInner {


    @SerializedName("formatted_address")
    private String formatted_address;
    @SerializedName("name")
    private String name;
    @SerializedName("geometry")
    private CategoriesResultInner geometryData;
    @SerializedName("location")
    private CategoriesResultInner location;
    @SerializedName("photos")
    private ArrayList<CategoriesResultInner> photos;
    @SerializedName("0")
    private CategoriesResultInner photoszero;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @SerializedName("photo_reference")
    private String photo_reference;
    @SerializedName("rating")
    private double rating;
    @SerializedName("user_ratings_total")
    private int user_ratings_total;
    @SerializedName("opening_hours")
    private CategoriesResultInner opening_hours;
    @SerializedName("open_now")
    private boolean open_now;

    public CategoriesResultInner getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(CategoriesResultInner opening_hours) {
        this.opening_hours = opening_hours;
    }

    public boolean isOpen_now() {
        return open_now;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }

    public int getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(int user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<CategoriesResultInner> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<CategoriesResultInner> photos) {
        this.photos = photos;
    }

    public CategoriesResultInner getPhotoszero() {
        return photoszero;
    }

    public void setPhotoszero(CategoriesResultInner photoszero) {
        this.photoszero = photoszero;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoriesResultInner getGeometryData() {
        return geometryData;
    }

    public void setGeometryData(CategoriesResultInner geometryData) {
        this.geometryData = geometryData;
    }

    public CategoriesResultInner getLocation() {
        return location;
    }

    public void setLocation(CategoriesResultInner location) {
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
