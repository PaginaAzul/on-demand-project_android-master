package com.pagin.azul.onphasesecond.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RatingResponse implements Parcelable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("resAndStoreId")
    private String resAndStoreId;

    @SerializedName("rating")
    private String rating;

    @SerializedName("review")
    private String review;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("userData")
    private RatingResponse userData;

    @SerializedName("profilePic")
    private String profilePic;

    @SerializedName("name")
    private String name;

    protected RatingResponse(Parcel in) {
        _id = in.readString();
        resAndStoreId = in.readString();
        rating = in.readString();
        review = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        userData = in.readParcelable(RatingResponse.class.getClassLoader());
        profilePic = in.readString();
        name = in.readString();
    }

    public static final Creator<RatingResponse> CREATOR = new Creator<RatingResponse>() {
        @Override
        public RatingResponse createFromParcel(Parcel in) {
            return new RatingResponse(in);
        }

        @Override
        public RatingResponse[] newArray(int size) {
            return new RatingResponse[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getResAndStoreId() {
        return resAndStoreId;
    }

    public void setResAndStoreId(String resAndStoreId) {
        this.resAndStoreId = resAndStoreId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RatingResponse getUserData() {
        return userData;
    }

    public void setUserData(RatingResponse userData) {
        this.userData = userData;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(resAndStoreId);
        dest.writeString(rating);
        dest.writeString(review);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeParcelable(userData, flags);
        dest.writeString(profilePic);
        dest.writeString(name);
    }
}
