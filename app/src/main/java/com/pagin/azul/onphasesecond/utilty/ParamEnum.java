package com.pagin.azul.onphasesecond.utilty;

/**
 * Created by Mahipal on 2/11/18.
 */
public enum ParamEnum {

    ADDRESS("Address"),
    RESTAURANT("Restaurant"),
    GROCERY("Grocery Store"),
    TITLE("title"),
    ID("id"),
    ORDER_ID("order_id"),
    RES_AND_STORE_ID("resAndStoreId"),
    CUISINE("cuisine"),
    DATA("data"),
    DATA_LIST("data_list"),
    VEG("Veg"),
    COUNT("count"),
    FILTER_ITEM("filter_item"),
    FILTER_SUB_ITEM("filter_sub_item"),
    UPCOMING("Upcoming"),
    ON_GOING("Ongoing"),
    PAST("Past"),
    LAT("Lat"),
    LONG("Long"),
    SUB_CATEGORY_NAME("subCategoryName"),
    PRODUCT("Product"),
    TYPE("type");


    private final String value;

    ParamEnum(String value) {
        this.value=value;
    }

    ParamEnum(){
     this.value=null;
    }

    public String theValue() {
        return this.value;
    }


}