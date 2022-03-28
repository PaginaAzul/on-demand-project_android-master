package com.pagin.azul.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by mobulous06 on 19/7/17.
 */

public class ApiClientConnection {
    ServiceProgressDialog serviceProgressDialog;
    private static ApiClientConnection connect;
    private ApiInterface clientService;
    public static final String Base_URL = "http://3.129.47.202:3000/api/v1/user/";   // PaginAzul url
    //public static final String Base_URL = "http://3.129.47.202:3021/api/v1/user/";    // on demand url
    //public static final String Base_URL = "http://3.128.74.178:3000/api/v1/user/";
    //public static final String Base_URL = "http://18.189.223.53:3000/api/v1/user/";
    //public static final String Base_URL = "http://192.168.1.41:3000/api/v1/user/";



    private static ApiClientConnection apiClientConnection = null;
    private static ApiInterface apiInterface = null, distanceMatrixInterface = null;

    public static ApiClientConnection getInstance() {
        if (apiClientConnection == null) {
            apiClientConnection = new ApiClientConnection();
        }
        return apiClientConnection;
    }


    public ApiInterface createApiInterface() {
        if (apiInterface == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                    .connectTimeout(80, TimeUnit.SECONDS)
                    .readTimeout(80, TimeUnit.SECONDS);

            httpBuilder.addInterceptor(loggingInterceptor);
            /*httpBuilder.addNetworkInterceptor(loggingInterceptor);
            httpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("langCode", "ar");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });*/

//            Gson gson = new GsonBuilder()
//                    .setLenient()
//                    .create();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpBuilder.build());

            Retrofit retrofit = builder.build();
            apiInterface = retrofit.create(ApiInterface.class);


        }
        return apiInterface;
    }

    public ApiInterface createMapApiInterface() {
        if (distanceMatrixInterface == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
                    .connectTimeout(80, TimeUnit.SECONDS)
                    .readTimeout(80, TimeUnit.SECONDS);

            httpBuilder.addInterceptor(loggingInterceptor);
//            Gson gson = new GsonBuilder()
//                    .setLenient()
//                    .create();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpBuilder.build());

            Retrofit retrofit = builder.build();
            distanceMatrixInterface = retrofit.create(ApiInterface.class);


        }
        return distanceMatrixInterface;
    }
}
