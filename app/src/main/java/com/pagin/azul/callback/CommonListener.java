package com.pagin.azul.callback;

import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.onphasesecond.model.MenuItem;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.model.ProductDetailsResponse;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;

import java.util.ArrayList;

public interface CommonListener {

    default void onItemClick(int position, ArrayList<CommonResponseBean> commonResponses){}

    default void onItemClick(int position){}

    default void onCategoryClick(int position,ArrayList<ProductResponse> categoryList){}

    default void onResAndStoreClick(int position, ArrayList<RestaurantResponse> restAndStoreList){}

    default void onFavClick(int position){}

    default void onFavClick(int position,ArrayList<RestaurantResponse> list){}

    default void onFavClick(int position, int homePosition, RestaurantResponse restaurantResponse){}

    default void onItemClick(int parentPosition,int childPosition){}

    default void onDeleteClick(int position){}

    default void onImageClick(int position){}

    default void onImageClick(String workImage){}

    default void onFetchLocation(String fullAddress,double the_lat,double the_long){}

    default void onViewMoreClick(int position){}

    default void onLastItemVisible(){}

    default void onAddItem(int position,ArrayList<RestaurantResponse> menuDataList){}

    default void onAddCart(int position,ArrayList<ProductDetailsResponse> menuDataList){}

    default void onRemoveItem(int position,ArrayList<RestaurantResponse> menuDataList){}

    default void onFilter(String newText){}

    default void onFilterClick(){}

    default void onFilterItemClick(int position, ArrayList<MenuItem> list){}

    default void onFilterSubItemClick(int position, ArrayList<MenuItem> list){}

    default void onMenuFilterClick(String cuisine){}

    default void onTimerFinish(int position, ArrayList<MyOrderResponse> data){}

    default void onCancelClick(int position, ArrayList<MyOrderResponse> data){}

    default void onReorderClick(int position, ArrayList<MyOrderResponse> data){}

    default void onProductClick(int position, ArrayList<RestaurantResponse> data){}

    default void onSelectMorningSlot(int position){}

    default void onSelectAfternoonSlot(int position){}

    default void onSelectEveningSlot(int position){}

    default void onApplyFilter(int radioButtonId, String validityDate){}

}
