package com.pagin.azul.bean;

public class NavDrawerModel {
    private String itemName;
    private int image;

    public NavDrawerModel(String itemName, int image) {
        this.itemName = itemName;
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
