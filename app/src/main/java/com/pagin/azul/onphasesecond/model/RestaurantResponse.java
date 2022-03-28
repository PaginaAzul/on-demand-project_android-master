package com.pagin.azul.onphasesecond.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RestaurantResponse implements Parcelable {

    @SerializedName("restaurantList")
    private ArrayList<RestaurantResponse> restaurantList;

    @SerializedName("storeList")
    private ArrayList<RestaurantResponse> storeList;

    @SerializedName("productList")
    private ArrayList<RestaurantResponse> productList;

    @SerializedName("_id")
    private String _id;

    @SerializedName("resAndStoreId")
    private String resAndStoreId;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("totalRating")
    private String totalRating;

    @SerializedName("avgRating")
    private String avgRating;

    @SerializedName("cuisinesName")
    private ArrayList<String> cuisinesName;

    @SerializedName("address")
    private String address;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("distance")
    private String distance;

    @SerializedName("isFav")
    private boolean isFav;

    @SerializedName("sellerData")
    private RestaurantResponse sellerData;

    @SerializedName("dist")
    private RestaurantResponse dist;

    @SerializedName("resAndStoreDetail")
    private RestaurantResponse resAndStoreDetail;

    @SerializedName("calculated")
    private double calculated;

    @SerializedName("deliveryTime")
    private String deliveryTime;

    @SerializedName("minimumValue")
    private String minimumValue;

    @SerializedName("cuisine")
    private String cuisine;

    @SerializedName("offerStatus")

    private String offerStatus;

    @SerializedName("menuList")
    private ArrayList<RestaurantResponse> menuList;

    @SerializedName("cuisineData")
    private ArrayList<RestaurantResponse> cuisineData;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productType")
    private String productType;

    @SerializedName("price")
    private String price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("productImage")
    private String productImage;

    @SerializedName("description")
    private String description;

    @SerializedName("openingTime")
    private String openingTime;

    @SerializedName("closingTime")
    private String closingTime;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("rating")
    private ArrayList<RatingResponse> rating;

    @SerializedName("mainService")
    private ArrayList<RestaurantResponse> mainService;

    @SerializedName("homeBanner")
    private RestaurantResponse homeBanner;

    @SerializedName("productData")
    private RestaurantResponse productData;

    @SerializedName("englishName")
    private String englishName;

    @SerializedName("portName")
    private String portName;

    @SerializedName("productId")
    private String productId;

    @SerializedName("deliveryCharge")
    private String deliveryCharge;

    @SerializedName("commission")
    private String commission;

    @SerializedName("cartData")
    private RestaurantResponse cartData;

    @SerializedName("storeType")
    private String storeType;

    @SerializedName("measurement")
    private String measurement;

    @SerializedName("offerPrice")
    private String offerPrice;

    @SerializedName("oStatus")
    private String oStatus;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    public RestaurantResponse(){

    }


    protected RestaurantResponse(Parcel in) {
        restaurantList = in.createTypedArrayList(RestaurantResponse.CREATOR);
        storeList = in.createTypedArrayList(RestaurantResponse.CREATOR);
        _id = in.readString();
        resAndStoreId = in.readString();
        name = in.readString();
        image = in.readString();
        totalRating = in.readString();
        avgRating = in.readString();
        cuisinesName = in.createStringArrayList();
        address = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        distance = in.readString();
        isFav = in.readByte() != 0;
        sellerData = in.readParcelable(RestaurantResponse.class.getClassLoader());
        dist = in.readParcelable(RestaurantResponse.class.getClassLoader());
        resAndStoreDetail = in.readParcelable(RestaurantResponse.class.getClassLoader());
        calculated = in.readDouble();
        deliveryTime = in.readString();
        minimumValue = in.readString();
        cuisine = in.readString();
        menuList = in.createTypedArrayList(RestaurantResponse.CREATOR);
        productName = in.readString();
        productType = in.readString();
        price = in.readString();
        currency = in.readString();
        productImage = in.readString();
        description = in.readString();
        openingTime = in.readString();
        closingTime = in.readString();
        quantity = in.readInt();
        rating = in.createTypedArrayList(RatingResponse.CREATOR);
        mainService = in.createTypedArrayList(RestaurantResponse.CREATOR);
        homeBanner = in.readParcelable(RestaurantResponse.class.getClassLoader());
        productData = in.readParcelable(RestaurantResponse.class.getClassLoader());
        englishName = in.readString();
        portName = in.readString();
        productId = in.readString();
        deliveryCharge = in.readString();
        commission = in.readString();
        cartData = in.readParcelable(RestaurantResponse.class.getClassLoader());
    }

    public static final Creator<RestaurantResponse> CREATOR = new Creator<RestaurantResponse>() {
        @Override
        public RestaurantResponse createFromParcel(Parcel in) {
            return new RestaurantResponse(in);
        }

        @Override
        public RestaurantResponse[] newArray(int size) {
            return new RestaurantResponse[size];
        }
    };

    public ArrayList<RestaurantResponse> getCuisineData() {
        return cuisineData;
    }

    public void setCuisineData(ArrayList<RestaurantResponse> cuisineData) {
        this.cuisineData = cuisineData;
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

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public ArrayList<RestaurantResponse> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<RestaurantResponse> productList) {
        this.productList = productList;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public RestaurantResponse getCartData() {
        return cartData;
    }

    public void setCartData(RestaurantResponse cartData) {
        this.cartData = cartData;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public RestaurantResponse getProductData() {
        return productData;
    }

    public void setProductData(RestaurantResponse productData) {
        this.productData = productData;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public ArrayList<RestaurantResponse> getMainService() {
        return mainService;
    }

    public void setMainService(ArrayList<RestaurantResponse> mainService) {
        this.mainService = mainService;
    }

    public RestaurantResponse getHomeBanner() {
        return homeBanner;
    }

    public void setHomeBanner(RestaurantResponse homeBanner) {
        this.homeBanner = homeBanner;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public ArrayList<RatingResponse> getRating() {
        return rating;
    }

    public void setRating(ArrayList<RatingResponse> rating) {
        this.rating = rating;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine() {
        return cuisine;
    }

    public ArrayList<RestaurantResponse> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<RestaurantResponse> menuList) {
        this.menuList = menuList;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
    }

    public RestaurantResponse getResAndStoreDetail() {
        return resAndStoreDetail;
    }

    public void setResAndStoreDetail(RestaurantResponse resAndStoreDetail) {
        this.resAndStoreDetail = resAndStoreDetail;
    }

    public RestaurantResponse getDist() {
        return dist;
    }

    public void setDist(RestaurantResponse dist) {
        this.dist = dist;
    }

    public double getCalculated() {
        return calculated;
    }

    public void setCalculated(double calculated) {
        this.calculated = calculated;
    }

    public String getResAndStoreId() {
        return resAndStoreId;
    }

    public void setResAndStoreId(String resAndStoreId) {
        this.resAndStoreId = resAndStoreId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public RestaurantResponse getSellerData() {
        return sellerData;
    }

    public void setSellerData(RestaurantResponse sellerData) {
        this.sellerData = sellerData;
    }

    public ArrayList<RestaurantResponse> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<RestaurantResponse> storeList) {
        this.storeList = storeList;
    }

    public ArrayList<RestaurantResponse> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(ArrayList<RestaurantResponse> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public ArrayList<String> getCuisinesName() {
        return cuisinesName;
    }

    public void setCuisinesName(ArrayList<String> cuisinesName) {
        this.cuisinesName = cuisinesName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(restaurantList);
        parcel.writeTypedList(storeList);
        parcel.writeString(_id);
        parcel.writeString(resAndStoreId);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(totalRating);
        parcel.writeString(avgRating);
        parcel.writeStringList(cuisinesName);
        parcel.writeString(address);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeString(distance);
        parcel.writeByte((byte) (isFav ? 1 : 0));
        parcel.writeParcelable(sellerData, i);
        parcel.writeParcelable(dist, i);
        parcel.writeParcelable(resAndStoreDetail, i);
        parcel.writeDouble(calculated);
        parcel.writeString(deliveryTime);
        parcel.writeString(minimumValue);
        parcel.writeString(cuisine);
        parcel.writeTypedList(menuList);
        parcel.writeString(productName);
        parcel.writeString(productType);
        parcel.writeString(price);
        parcel.writeString(currency);
        parcel.writeString(productImage);
        parcel.writeString(description);
        parcel.writeString(openingTime);
        parcel.writeString(closingTime);
        parcel.writeInt(quantity);
        parcel.writeTypedList(rating);
        parcel.writeTypedList(mainService);
        parcel.writeParcelable(homeBanner, i);
        parcel.writeParcelable(productData, i);
        parcel.writeString(englishName);
        parcel.writeString(portName);
        parcel.writeString(productId);
        parcel.writeString(deliveryCharge);
        parcel.writeString(commission);
        parcel.writeParcelable(cartData, i);
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getoStatus() {
        return oStatus;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }
}
