package com.pagin.azul.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CommonResponse implements Parcelable {

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("subCategoryName")
    private String subCategoryName;

    @SerializedName("_id")
    private String _id;

    @SerializedName("subCategoryData")
    private CommonResponse[] subCategoryData;

    protected CommonResponse(Parcel in) {
        categoryName = in.readString();
        subCategoryName = in.readString();
        _id = in.readString();
        subCategoryData = in.createTypedArray(CommonResponse.CREATOR);
    }

    public static final Creator<CommonResponse> CREATOR = new Creator<CommonResponse>() {
        @Override
        public CommonResponse createFromParcel(Parcel in) {
            return new CommonResponse(in);
        }

        @Override
        public CommonResponse[] newArray(int size) {
            return new CommonResponse[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public CommonResponse[] getSubCategoryData() {
        return subCategoryData;
    }

    public void setSubCategoryData(CommonResponse[] subCategoryData) {
        this.subCategoryData = subCategoryData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeString(subCategoryName);
        parcel.writeString(_id);
        parcel.writeTypedArray(subCategoryData, i);
    }
}
