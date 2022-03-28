package com.pagin.azul.bean;

import com.google.gson.annotations.SerializedName;

public class RequestOrderInner {

    @SerializedName("status")
    private String status;

    @SerializedName("_id")
    private String _id;

    @SerializedName("selectCategoryName")
    private String selectCategoryName;

    @SerializedName("portugueseCategoryName")
    private String portugueseCategoryName;

    public String getSelectCategoryName() {
        return selectCategoryName;
    }

    public void setSelectCategoryName(String selectCategoryName) {
        this.selectCategoryName = selectCategoryName;
    }

    public String getPortugueseCategoryName() {
        return portugueseCategoryName;
    }

    public void setPortugueseCategoryName(String portugueseCategoryName) {
        this.portugueseCategoryName = portugueseCategoryName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
