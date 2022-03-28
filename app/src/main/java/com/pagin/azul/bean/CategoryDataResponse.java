package com.pagin.azul.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryDataResponse implements Serializable
{

    @SerializedName("createdAt")
    @Expose
    private String createdAt;


    @SerializedName("createdAt1")
    @Expose
    private String createdAt1;


    @SerializedName("_id")
    @Expose
    private String id;


    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("subCategoryName")
    @Expose
    private String subCategoryName;

@SerializedName("subSubCategoryName")
    @Expose
    private String subSubCategoryName;


    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    private final static long serialVersionUID = 4299743585883542187L;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt1() {
        return createdAt1;
    }

    public void setCreatedAt1(String createdAt1) {
        this.createdAt1 = createdAt1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubSubCategoryName() {
        return subSubCategoryName;
    }

    public void setSubSubCategoryName(String subSubCategoryName) {
        this.subSubCategoryName = subSubCategoryName;
    }
}