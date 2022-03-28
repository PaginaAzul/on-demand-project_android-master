package com.pagin.azul.retrofit;


import com.pagin.azul.bean.AddressApi;
import com.pagin.azul.bean.CateResponse;
import com.pagin.azul.bean.CategoryResponse;
import com.pagin.azul.bean.Changelanguage;
import com.pagin.azul.bean.ChatResponse;
import com.pagin.azul.bean.ClearCartResponse;
import com.pagin.azul.bean.CommanResponse;
import com.pagin.azul.bean.CommonModel;
import com.pagin.azul.bean.CommonModelBean;
import com.pagin.azul.bean.CreateInvoiceResponse;
import com.pagin.azul.bean.DeleteAddress;
import com.pagin.azul.bean.DeliveryPersonResponse;
import com.pagin.azul.bean.GetAddressList;
import com.pagin.azul.bean.GetLocation;
import com.pagin.azul.bean.MessageTrackResponse;
import com.pagin.azul.bean.MyProfileResponse;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.bean.NumberVerificationResponse;
import com.pagin.azul.bean.OrderDetailResponse;
import com.pagin.azul.bean.OrderResponse;
import com.pagin.azul.bean.OtpResendResponse;
import com.pagin.azul.bean.ProfessionalWorkerReponse;
import com.pagin.azul.bean.ProfessionalWorkerResponse;
import com.pagin.azul.bean.ReasonResponse;
import com.pagin.azul.bean.RequestOrderResponse;
import com.pagin.azul.bean.SettingResponse;
import com.pagin.azul.bean.SignInResponse;
import com.pagin.azul.bean.SignUpResponse;
import com.pagin.azul.bean.SignUpWithMobileResp;
import com.pagin.azul.bean.StaticMainResponse;
import com.pagin.azul.bean.UpdateSettingNoti;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.bean.ViewAllOfferResponse;
import com.pagin.azul.onphasesecond.model.ExclusiveOfferModel;
import com.pagin.azul.onphasesecond.model.FavoriteModel;

import com.pagin.azul.onphasesecond.model.MyOrderModel;

import com.pagin.azul.onphasesecond.model.GetCartCountModel;
import com.pagin.azul.onphasesecond.model.GetCustomerStoryDataModel;

import com.pagin.azul.onphasesecond.model.ProductDetailModel;
import com.pagin.azul.onphasesecond.model.ProductListResponse;
import com.pagin.azul.onphasesecond.model.ProductModel;
import com.pagin.azul.onphasesecond.model.RestaurantModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


/**
 * Created by mobulous06 on 19/7/17.
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("checkAvailability")
    Call<NumberVerificationResponse> numberVerification(@Field("countryCode") String countryCode,
                                                        @Field("mobileNumber") String mobileNumber,
                                                        @Field("langCode") String langCode);

    @FormUrlEncoded
    @POST("checkNumberForSignin")
    Call<NumberVerificationResponse> checkNumberForSignin(@Field("countryCode") String countryCode,
                                                          @Field("mobileNumber") String mobileNumber,
                                                          @Field("userType") String userType,
                                                          @Field("langCode") String langCode);

    @GET("getStaticContent")
    Call<SignUpResponse> getStaticContent();

    @Multipart
    @POST("signup")
    Call<SignUpWithMobileResp> signup(@PartMap Map<String, RequestBody> map,
                                      @Part MultipartBody.Part image);

    @Multipart
    @POST("signup")
    Call<SignUpWithMobileResp> signup(@PartMap Map<String, RequestBody> map);

    @FormUrlEncoded
    @POST("signupWithMobile")
    Call<SignUpWithMobileResp> signUpWithMobile(@Field("countryCode") String countryCode,
                                                @Field("mobileNumber") String mobileNumber);

    @FormUrlEncoded
    @POST("signupWithMobile")
    Call<CommanResponse> otpVerification(@Field("mobileOtp") String mobileOtp,
                                         @Field("userId") String userId);

    @FormUrlEncoded
    @POST("resendMobileOtp")
    Call<OtpResendResponse> otpResend(@Field("userId") String userId,
                                      @Field("mobileNumber") String mobileNumber,
                                      @Field("countryCode") String countryCode);

    @FormUrlEncoded
    @POST("deliveryPerson")
    Call<SignUpResponse> deliveryPerson1(@Field("aboutUs") String aboutUs,
                                         @Field("vehicleNumber") String vehicleNumber,
                                         @Field("vehicleType") String vehicleType,
                                         @Field("Insurance") String Insurance,
                                         @Field("bankAc") String bankAc,
                                         @Field("emergencyContact") String emergencyContact,
                                         @Field("id1") String id1,
                                         @Field("id2") String id2,
                                         @Field("vehicl eLicence") String vehicleLicence,
                                         @Field("userType") String userType,
                                         @Field("userId") String userId,
                                         @Field("profilePic") String profilePic);

    @Multipart
    @POST("deliveryPerson")
    Call<ProfessionalWorkerResponse> deliveryPerson(@PartMap Map<String, RequestBody> partMapData,
                                                    @Part MultipartBody.Part id1,
                                                    @Part MultipartBody.Part id2,
                                                    @Part MultipartBody.Part vehicleLicense,
                                                    @Part MultipartBody.Part insurance,
                                                    @Part MultipartBody.Part profilePic);

    @Multipart
    @POST("updateDeliveryPerson")
    Call<ProfessionalWorkerResponse> updateDeliveryPerson(@PartMap Map<String, RequestBody> partMapData,
                                                          @Part MultipartBody.Part id1,
                                                          @Part MultipartBody.Part id2,
                                                          @Part MultipartBody.Part vehicleLicense,
                                                          @Part MultipartBody.Part insurance,
                                                          @Part MultipartBody.Part profilePic);

    @Multipart
    @POST("updateDeliveryPerson")
    Call<ProfessionalWorkerResponse> updateDeliveryPersonwithoutPic(@PartMap Map<String, RequestBody> partMapData,
                                                          @Part MultipartBody.Part id1,
                                                          @Part MultipartBody.Part id2,
                                                          @Part MultipartBody.Part vehicleLicense,
                                                          @Part MultipartBody.Part insurance);

    @Multipart
    @POST("professionalPerson")
    Call<ProfessionalWorkerResponse> professionalPerson(@PartMap Map<String, RequestBody> partMapData,
                                                        @Part MultipartBody.Part Id1,
                                                        @Part MultipartBody.Part Id2,
                                                        @Part MultipartBody.Part Id3,
                                                        @Part MultipartBody.Part Id4,
                                                        @Part MultipartBody.Part profilePic);

    @Multipart
    @POST("updateProfessionalPerson")
    Call<ProfessionalWorkerResponse> updateProfessionalPerson(@PartMap Map<String, RequestBody> partMapData,
                                                              @Part MultipartBody.Part Id1,
                                                              @Part MultipartBody.Part Id2,
                                                              @Part MultipartBody.Part Id3,
                                                              @Part MultipartBody.Part Id4,
                                                              @Part MultipartBody.Part profilePic);

    @Multipart
    @POST("updateProfessionalPerson")
    Call<ProfessionalWorkerResponse> updateProfessionalPersonwithoutPic(@PartMap Map<String, RequestBody> partMapData,
                                                              @Part MultipartBody.Part Id1,
                                                              @Part MultipartBody.Part Id2,
                                                              @Part MultipartBody.Part Id3,
                                                              @Part MultipartBody.Part Id4);

    @Multipart
    @POST("professionalPerson")
    Call<ProfessionalWorkerResponse> professionalPerson(@PartMap Map<String, RequestBody> partMapData,
                                                        @Part MultipartBody.Part Id1,
                                                        @Part MultipartBody.Part Id2,
                                                        @Part MultipartBody.Part Id3,
                                                        @Part MultipartBody.Part profilePic);

    @Multipart
    @POST("professionalPerson")
    Call<ProfessionalWorkerResponse> professionalPerson(@PartMap Map<String, RequestBody> partMapData,
                                                        @Part MultipartBody.Part Id1,
                                                        @Part MultipartBody.Part Id2,
                                                        @Part MultipartBody.Part profilePic);

    @FormUrlEncoded
    @POST("signin")
    Call<SignInResponse> signIn(@Field("deviceToken") String deviceToken,
                                @Field("deviceType") String deviceType,
                                @Field("countryCode") String countryCode,
                                @Field("mobileNumber") String mobileNumber);

    @Multipart
    @POST("updateProfile")
    Call<ProfessionalWorkerResponse> updateProfile(@PartMap Map<String, RequestBody> partMapData,
                                                   @Part MultipartBody.Part profilePic);

    @Multipart
    @POST("updateProfile")
    Call<ProfessionalWorkerResponse> updateProfile(@PartMap Map<String, RequestBody> partMapData);

    @GET("getStaticContent")
    Call<StaticMainResponse> staticContent();

    @POST("updateSetting")
    Call<UpdateSettingNoti> onOff(@Header("token") String token, @Body RequestBody body);

    @POST("getUserDetails")
    Call<MyProfileResponse> getUserDetails(@Header("token") String token, @Body RequestBody body);

    @POST("getDeliveryDetails")
    Call<DeliveryPersonResponse> getDeliveryDetails(@Body RequestBody body);


    @POST("getProfessionalDetails")
    Call<ProfessionalWorkerReponse> getProfessionalDetails(@Header("token") String token, @Body RequestBody body);

    @POST("addAddress")
    Call<AddressApi> addAddress(@Header("token") String token, @Body RequestBody body);

    @POST("getAddress")
    Call<GetAddressList> getAddressList(@Header("token") String token, @Body RequestBody body);

    @POST("updateAddress")
    Call<UpdateSettingNoti> updateAddress(@Header("token") String token, @Body RequestBody body);


    @POST("deleteAddress")
    Call<DeleteAddress> deleteAddress(@Header("token") String token, @Body RequestBody body);

    @POST("changeLanguage")
    Call<Changelanguage> changelang(@Header("token") String token, @Body RequestBody body);

    @GET("getCategory")
    Call<CategoryResponse> getCategory();

    @FormUrlEncoded
    @POST("getSubCategory")
    Call<CategoryResponse> getSubCategory(@Header("token") String token, @Field("categoryId") String categoryId);

    @FormUrlEncoded
    @POST("getSubSubCategory")
    Call<CategoryResponse> getSubSubCategory(@Header("token") String token, @Field("subCategoryId") String subCategoryId);

    @POST("mobileNumberChange")
    Call<MyProfileResponse> mobileNumberChange(@Header("token") String token, @Body RequestBody body);

    @POST("getTotal")
    Call<GetLocation> getLocation(@Header("token") String token, @Body RequestBody body);

    @POST("getOrder")
    Call<OrderDetailResponse> getOrder(@Header("token") String token, @Body RequestBody body);

    @POST("requestOrder")
    Call<RequestOrderResponse> requestOrder(@Header("token") String token, @Body RequestBody body);

    @Multipart
    @POST("requestOrder")
    Call<RequestOrderResponse> requestOrder(@Header("token") String token,
                                            @PartMap Map<String, RequestBody> part,
                                            @Part List<MultipartBody.Part> group_image);
    @Multipart
    @POST("requestOrder")
    Call<RequestOrderResponse> requestOrder(@Header("token") String token,
                                            @PartMap Map<String, RequestBody> part);

    @POST("checkUserType")
    Call<UserTypeResponse> checkUserType(@Header("token") String token, @Body RequestBody body);


    @POST("getNormalUserPendingOrder")
    Call<NormalUserPendingOrderResponse> getPendingOrder(@Header("token") String token, @Body RequestBody body);


    @POST("logout")
    Call<MyProfileResponse> logOut(@Header("token") String token, @Body RequestBody body);

    @POST("orderCancel")
    Call<UserTypeResponse> orderCancel(@Header("token") String token, @Body RequestBody body);

    @POST("orderReport")
    Call<UserTypeResponse> orderReport(@Header("token") String token, @Body RequestBody body);

    @POST("orderReportByNormalUser")
    Call<UserTypeResponse> orderReportByNormalUser(@Header("token") String token, @Body RequestBody body);

    @POST("getOfferList")
    Call<NormalUserPendingOrderResponse> getOfferList(@Header("token") String token, @Body RequestBody body);

    @POST("updateUserId")
    Call<MyProfileResponse> updateUserId(@Header("token") String token, @Body RequestBody body);

    @POST("getNewOrderForDeliveryPerson")
    Call<NormalUserPendingOrderResponse> getNODeliveryPerson(@Header("token") String token, @Body RequestBody body);

    @POST("getPendingOrderDeliveryPerson")
    Call<NormalUserPendingOrderResponse> getPendingOrderDP(@Header("token") String token, @Body RequestBody body);

    @POST("makeOffer")
    Call<RequestOrderResponse> makeOffer(@Header("token") String token, @Body RequestBody body);

    @POST("orderCancelFromDelivery")
    Call<RequestOrderResponse> orderCancelFromDelivery(@Header("token") String token, @Body RequestBody body);

    @POST("orderWithdrawFromDeliveryAndPro")
    Call<RequestOrderResponse> orderWithdrawFromDeliveryAndPro(@Header("token") String token, @Body RequestBody body);


    @POST("acceptOffer")
    Call<ViewAllOfferResponse> acceptOffer(@Header("token") String token, @Body RequestBody body);

    @POST("getActiveOrderDeliveryPerson")
    Call<NormalUserPendingOrderResponse> getActiveOrderDeliveryPerson(@Header("token") String token, @Body RequestBody body);


    @POST("getNormalUserActiveOrder")
    Call<NormalUserPendingOrderResponse> getNormalUserActiveOrder(@Header("token") String token, @Body RequestBody body);

    @POST("workDoneByDeliveryPerson")
    Call<NormalUserPendingOrderResponse> workDoneByDeliveryPerson(@Header("token") String token, @Body RequestBody body);

    @POST("getPastOrderDeliveryPerson1")
    Call<NormalUserPendingOrderResponse> getPastOrderDeliveryPerson(@Header("token") String token, @Body RequestBody body);

    @POST("getPastOrderForNormalUser1")
    Call<NormalUserPendingOrderResponse> getNormalUserPastOrder(@Header("token") String token, @Body RequestBody body);

    @POST("rating")
    Call<SignUpWithMobileResp> rating(@Header("token") String token, @Body RequestBody body);

    @POST("getAllRating")
    Call<NormalUserPendingOrderResponse> getAllRating(@Header("token") String token, @Body RequestBody body);

    @POST("goStatus")
    Call<NormalUserPendingOrderResponse> goStatus(@Header("token") String token, @Body RequestBody body);

    @POST("getInvoiceDetails")
    Call<CreateInvoiceResponse> getInvoiceDetails(@Header("token") String token, @Body RequestBody body);

    @Multipart
    @POST("createInvoiceByDeliveryPerson")
    Call<ProfessionalWorkerResponse> createInvoiceByDeliveryPerson(@PartMap Map<String, RequestBody> partMapData,
                                                                   @Part MultipartBody.Part invoiceImg);

    @Multipart
    @POST("createInvoiceByDeliveryPerson")
    Call<ProfessionalWorkerResponse> createInvoiceByDeliveryPerson(@PartMap Map<String, RequestBody> partMapData);


    @POST("getSetting")
    Call<SettingResponse> getSetting(@Header("token") String token, @Body RequestBody body);

    @POST("getRate")
    Call<NormalUserPendingOrderResponse> getRate(@Header("token") String token, @Body RequestBody body);

    @POST("getNewOrderForProfessionalWorker")
    Call<NormalUserPendingOrderResponse> getNewOrderForProfessionalWorker(@Header("token") String token, @Body RequestBody body);

    @POST("makeAOfferByProfessionalWorker")
    Call<RequestOrderResponse> makeAOfferByProfessionalWorker(@Header("token") String token, @Body RequestBody body);

    @POST("getPendingOrderProfessionalWorker")
    Call<NormalUserPendingOrderResponse> getPendingOrderPWorker(@Header("token") String token, @Body RequestBody body);

    @POST("getActiveOrderProfessionalWorker")
    Call<NormalUserPendingOrderResponse> getActiveOrderPWorker(@Header("token") String token, @Body RequestBody body);

    @POST("getPastOrderForProfessionalWorker1")
    Call<NormalUserPendingOrderResponse> getPastOrderPWorker(@Header("token") String token, @Body RequestBody body);


    @POST("workDoneByProfessionalWorker")
    Call<NormalUserPendingOrderResponse> workDoneByProfessionalWorker(@Header("token") String token, @Body RequestBody body);

    @POST("arrivedStatus")
    Call<NormalUserPendingOrderResponse> arrivedStatus(@Header("token") String token, @Body RequestBody body);

    @POST("getNotificationList")
    Call<NormalUserPendingOrderResponse> notificationList(@Header("token") String token, @Body RequestBody body);

    @POST("getChatHistory")
    Call<ChatResponse> getChatHistory(@Header("token") String token, @Body RequestBody body);

    @POST("updateLocation")
    Call<ChatResponse> updateLocation(@Header("token") String token, @Body RequestBody body);

    @POST("getInvoicDetails")
    Call<NormalUserPendingOrderResponse> getInvoicDetails(@Header("token") String token, @Body RequestBody body);

    @POST("contactUs")
    Call<UserTypeResponse> contactUs(@Header("token") String token, @Body RequestBody body);

    @POST("rejectOffer")
    Call<ViewAllOfferResponse> rejectOffer(@Header("token") String token, @Body RequestBody body);

    @POST("deliveryActiveOrder")
    Call<MessageTrackResponse> deliveryActiveOrder(@Body RequestBody body);

    @POST("normalActiveOrder")
    Call<MessageTrackResponse> normalActiveOrder(@Body RequestBody body);

    @POST("updatePopupStatus")
    Call<MessageTrackResponse> updatePopupStatus(@Body RequestBody body);

    @POST("changeDeliveryCaptain")
    Call<MessageTrackResponse> changeDeliveryCaptain(@Header("token") String token, @Body RequestBody body);


    @GET("http://18.217.0.63:3000/api/v1/user/getReportReason")
    Call<ReasonResponse> reasonType();

    @GET("orderCategoryList")
    Call<CommonModel> orderCategoryList();

    @GET("orderCategoryList")
    Call<CommonModelBean> getOrderCategoryList();

    @POST("acceptWithdrawOrderRequest")
    Call<RequestOrderResponse> acceptWithdrawOrderRequest(@Header("token") String token, @Body RequestBody body);

    @POST("declineWithdrawOrderRequest")
    Call<RequestOrderResponse> declineWithdrawOrderRequest(@Header("token") String token, @Body RequestBody body);

    @POST("checkCurrentOrder")
    Call<OrderResponse> checkCurrentOrder(@Header("token") String token, @Body RequestBody body);

    @POST("checkOrderAcceptOrNot")
    Call<RequestOrderResponse> checkOrderAcceptOrNot(@Header("token") String token, @Body RequestBody body);

    @POST("getRestaurantAndStoreData")
    Call<RestaurantModel> getRestaurantAndStoreData(@Header("token") String token, @Body RequestBody body);

    @POST("getRestaurantAndStoreDataForSearch")
    Call<RestaurantModel> getRestaurantAndStoreDataForSearch(@Header("token") String token, @Body RequestBody body);

    @POST("resAndStoreDetail")
    Call<RestaurantModel> resAndStoreDetail(@Header("token") String token, @Body RequestBody body);

    @POST("getFavouriteList")
    Call<FavoriteModel> getFavouriteList(@Header("token") String token, @Body RequestBody body);

    @POST("addToFavourite")
    Call<FavoriteModel> addToFavourite(@Header("token") String token, @Body RequestBody body);

    @POST("getRestaurantMenuList")
    Call<FavoriteModel> getRestaurantMenuList(@Header("token") String token, @Body RequestBody body);

    @POST("getCartItem")
    Call<FavoriteModel> getCartItem(@Header("token") String token, @Body RequestBody body);

    @POST("getMenuData")
    Call<RestaurantModel> getMenuData(@Header("token") String token, @Body RequestBody body);

    @POST("updateCart")
    Call<RestaurantModel> updateCart(@Header("token") String token, @Body RequestBody body);

    @POST("addToCart")
    Call<RestaurantModel> addToCart(@Header("token") String token, @Body RequestBody body);

    @POST("clearCart")
    Call<ClearCartResponse> clearCart(@Header("token") String token, @Body RequestBody body);

    @POST("getDeliveryCharge")
    Call<RestaurantModel> getDeliveryCharge(@Header("token") String token, @Body RequestBody body);

    @POST("placeOrder")
    Call<RestaurantModel> placeOrder(@Header("token") String token, @Body RequestBody body);

    @POST("fiterApi")
    Call<FavoriteModel> fiterApi(@Header("token") String token, @Body RequestBody body);

    @POST("getExclusiveOfferList")
    Call<ExclusiveOfferModel> getExclusiveOfferList(@Header("token") String token, @Body RequestBody body);

    @POST("getUserOrder")
    Call<MyOrderModel> getUserOrder(@Header("token") String token, @Body RequestBody body);

    @GET("getDashboardData")
    Call<RestaurantModel> getDashboardData();

    @POST("getCartCount")
    Call<GetCartCountModel> getCartCount(@Header("token") String token,@Body RequestBody body);

    @POST("getCustomerStory")
    Call<GetCustomerStoryDataModel> getCustomerStory(@Header("token") String token,@Body RequestBody body);

    @POST("updateCancelStatus")
    Call<MyOrderModel> updateCancelStatus(@Header("token") String token,@Body RequestBody body);

    @POST("cancelOrderByUser")
    Call<MyOrderModel> cancelOrderByUser(@Header("token") String token,@Body RequestBody body);

    @POST("resAndStoreRating")
    Call<SignUpWithMobileResp> resAndStoreRating(@Header("token") String token,@Body RequestBody body);

    @POST("reOrder")
    Call<RestaurantModel> reOrder(@Header("token") String token,@Body RequestBody body);

    @POST("getProductData")
    Call<RestaurantModel> getProductData(@Header("token") String token,@Body RequestBody body);

    @POST("productDetail")
    Call<ProductDetailModel> productDetail(@Header("token") String token, @Body RequestBody body);

    @POST("getCategoryList")
    Call<ProductModel> getCategoryList(@Body RequestBody body);

    @GET("getOfferCategory")
    Call<ProductModel> getOfferCategory();

    @POST("getSubCategoryList")
    Call<ProductModel> getSubCategoryList(@Body RequestBody body);

    @POST("getCuisineListForUser")
    Call<ProductModel> getCuisineListForUser(@Body RequestBody body);

    @POST("getOfferProductList")
    Call<ProductListResponse> getOfferProductList(@Body RequestBody body);

    @POST("getDeliverySlotList")
    Call<ProductModel> getDeliverySlotList(@Header("token") String token, @Body RequestBody body);


}
