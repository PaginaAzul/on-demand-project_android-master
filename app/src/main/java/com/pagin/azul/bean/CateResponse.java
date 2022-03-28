package com.pagin.azul.bean;

public class CateResponse {
    private String categoriesName;
    private int categoriesImg;

    public CateResponse(String categoriesName, int categoriesImg) {
        this.categoriesName = categoriesName;
        this.categoriesImg = categoriesImg;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }

    public int getCategoriesImg() {
        return categoriesImg;
    }

    public void setCategoriesImg(int categoriesImg) {
        this.categoriesImg = categoriesImg;
    }
}
