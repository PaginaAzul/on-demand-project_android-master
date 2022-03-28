package com.pagin.azul.onphasesecond.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductDetailsResponse implements Parcelable {

    @SerializedName("name")
    private String name;

    @SerializedName("totalRating")
    private String totalRating;

    @SerializedName("avgRating")
    private String avgRating;

    @SerializedName("productName")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("measurement")
    private String measurement;

    @SerializedName("price")
    private String price;

    @SerializedName("productImage")
    private String productImage;

    @SerializedName("currency")
    private String currency;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("type")
    private String type;

    @SerializedName("resAndStoreId")
    private ProductDetailsResponse resAndStoreId;

    @SerializedName("rating")
    private ArrayList<RatingResponse> rating;

    @SerializedName("_id")
    private String _id;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("offerPrice")
    private String offerPrice;

    @SerializedName("cartData")
    private RestaurantResponse cartData;

    public String getOfferPrice() {
        return offerPrice;
    }

    @SerializedName("oStatus")
    private String oStatus;

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    protected ProductDetailsResponse(Parcel in) {
        name = in.readString();
        totalRating = in.readString();
        avgRating = in.readString();
        productName = in.readString();
        description = in.readString();
        measurement = in.readString();
        price = in.readString();
        productImage = in.readString();
        currency = in.readString();
        quantity = in.readString();
        resAndStoreId = in.readParcelable(ProductDetailsResponse.class.getClassLoader());
    }

    public ProductDetailsResponse(){

    }

    public static final Creator<ProductDetailsResponse> CREATOR = new Creator<ProductDetailsResponse>() {
        @Override
        public ProductDetailsResponse createFromParcel(Parcel in) {
            return new ProductDetailsResponse(in);
        }

        @Override
        public ProductDetailsResponse[] newArray(int size) {
            return new ProductDetailsResponse[size];
        }
    };

    public RestaurantResponse getCartData() {
        return cartData;
    }

    public void setCartData(RestaurantResponse cartData) {
        this.cartData = cartData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<RatingResponse> getRating() {
        return rating;
    }

    public void setRating(ArrayList<RatingResponse> rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
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

    public String getoStatus() {
        return oStatus;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public ProductDetailsResponse getResAndStoreId() {
        return resAndStoreId;
    }

    public void setResAndStoreId(ProductDetailsResponse resAndStoreId) {
        this.resAndStoreId = resAndStoreId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(totalRating);
        parcel.writeString(avgRating);
        parcel.writeString(productName);
        parcel.writeString(description);
        parcel.writeString(measurement);
        parcel.writeString(price);
        parcel.writeString(productImage);
        parcel.writeString(currency);
        parcel.writeString(quantity);
        parcel.writeParcelable(resAndStoreId, i);
    }
}
