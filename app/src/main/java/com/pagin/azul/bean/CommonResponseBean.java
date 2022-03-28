package com.pagin.azul.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommonResponseBean implements Parcelable {

    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("portugueseCategoryName")
    private String portugueseCategoryName;

    @SerializedName("categoryImage")
    private String categoryImage;

    @SerializedName("subCategoryName")
    private String subCategoryName;

    @SerializedName("portugueseSubCategoryName")
    private String portugueseSubCategoryName;

    @SerializedName("_id")
    private String _id;

    @SerializedName("image")
    private String image;

    @SerializedName("subCategoryData")
    private ArrayList<CommonResponseBean> subCategoryData;

    public CommonResponseBean(){

    }

    protected CommonResponseBean(Parcel in) {
        categoryName = in.readString();
        subCategoryName = in.readString();
        _id = in.readString();
        categoryImage = in.readString();
        image = in.readString();
        subCategoryData = in.createTypedArrayList(CommonResponseBean.CREATOR);
        portugueseCategoryName = in.readString();
        portugueseSubCategoryName = in.readString();
    }

    public static final Creator<CommonResponseBean> CREATOR = new Creator<CommonResponseBean>() {
        @Override
        public CommonResponseBean createFromParcel(Parcel in) {
            return new CommonResponseBean(in);
        }

        @Override
        public CommonResponseBean[] newArray(int size) {
            return new CommonResponseBean[size];
        }
    };

    public String getPortugueseCategoryName() {
        return portugueseCategoryName;
    }

    public void setPortugueseCategoryName(String portugueseCategoryName) {
        this.portugueseCategoryName = portugueseCategoryName;
    }

    public String getPortugueseSubCategoryName() {
        return portugueseSubCategoryName;
    }

    public void setPortugueseSubCategoryName(String portugueseSubCategoryName) {
        this.portugueseSubCategoryName = portugueseSubCategoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

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

    public ArrayList<CommonResponseBean> getSubCategoryData() {
        return subCategoryData;
    }

    public void setSubCategoryData(ArrayList<CommonResponseBean> subCategoryData) {
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
        parcel.writeString(categoryImage);
        parcel.writeString(image);
        parcel.writeTypedList(subCategoryData);
        parcel.writeString(portugueseCategoryName);
        parcel.writeString(portugueseSubCategoryName);
    }

}
