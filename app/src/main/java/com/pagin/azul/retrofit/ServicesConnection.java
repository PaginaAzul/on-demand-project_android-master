package com.pagin.azul.retrofit;

/**
 * Created by mobulous2 on 15/3/17.
 */
public class ServicesConnection
{
//    ServiceProgressDialog serviceProgressDialog;
//    private static ServicesConnection connect;
//    private ApiInterface clientService;
//    public static final String BASE_URL = "http://192.168.1.15:3000/api/v1/user/";
//
//
//    public static final int DEFAULT_RETRIES = 0;
//
//    public static synchronized ServicesConnection getInstance()
//    {
//        if (connect == null) {
//            connect = new ServicesConnection();
//        }
//        return connect;
//    }
//
//    //    service interface instance to call api
//    public ApiInterface createService() throws Exception
//    {
//        if (clientService == null)
//        {
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();//    logs HTTP request and response data.
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);//  set your desired log level
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
////            httpClient.readTimeout(1, TimeUnit.SECONDS)
////                    .connectTimeout(1, TimeUnit.SECONDS);
//            httpClient.readTimeout(1, TimeUnit.MINUTES);
//            httpClient.readTimeout(1, TimeUnit.MINUTES);
//            // add your other interceptors …
//            httpClient.addInterceptor(logging); //  add logging as last interceptor
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient.build())
//                    .build();
//
//            clientService = retrofit.create(ApiInterface.class);
//        }
//        return clientService;
//    }
//
//    //    enqueue
//    public <T> boolean enqueueWithRetry(Call<T> call, final Activity activity, final boolean isLoader, final int retryCount, final Callback<T> callback)
//    {
//        if(MyApplication.networkConnectionCheck())
//        {
//            if(isLoader && activity!=null)
//            {
//
//                serviceProgressDialog=new ServiceProgressDialog(activity);
//                serviceProgressDialog.showCustomProgressDialog();
//            }
//            call.enqueue(new ServicesRetryableCallback<T>(call, retryCount)
//            {
//                @Override
//                public void onFinalResponse(Call<T> call, Response<T> response)
//                {
//                    if(serviceProgressDialog!=null)
//                    {
//                        serviceProgressDialog.hideProgressDialog();
//                    }
//                    /*if(serviceProgressDialog!=null)
//                    {
//                        serviceProgressDialog.hideProgressDialog();
//                        if(response.message().equalsIgnoreCase("Internal Server Error"))
//                        {
//                            Toast.makeText(activity," Internal Server Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }*/
////                    if(response.body() instanceof ServicesResponseBean)
////                    {
////                        if(((ServicesResponseBean)response.body()).getMessage().equals("Please login."))
////                        {
////                            Toast.makeText(activity,"This account already logged in from other device, Please logged in!", Toast.LENGTH_SHORT).show();
////
////
////                            //CustomToast.showCustomToast(activity, "Invalid User.");
////                        }
////                    }
//                    callback.onResponse(call, response);
//                }
//
//                @Override
//                public void onFinalFailure(Call<T> call, Throwable t)
//                {
//                    if(serviceProgressDialog!=null)
//                {
//                    serviceProgressDialog.hideProgressDialog();
//                }
//
//                    if (t instanceof ConnectException) {
//
//                        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
//
//                    } else if (t instanceof SocketTimeoutException) {
//
//                        Toast.makeText(activity, R.string.connection_lost, Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof UnknownHostException) {
//
//                        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
//                    } else if (t instanceof InternalError) {
//
//                        Toast.makeText(activity, R.string.server_error, Toast.LENGTH_SHORT).show();
//                    }
//
//                    callback.onFailure(call, t);
//                }
//            });
//            return true;
//        }
//        else
//        {
//            //if(isLoader)
//           // MyDialog.getInstance(activity).hideDialog();
//           /* serviceProgressDialog.hideProgressDialog();*/
//
//            Toast.makeText(activity, ""+activity.getString(R.string.interdis), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    }
//
//    public  <T> boolean enqueueWithoutRetry(Call<T> call, Activity activity, boolean isLoader, final Callback<T> callback) {
//        return enqueueWithRetry(call,  activity,isLoader, DEFAULT_RETRIES, callback);
//    }
}
//4B:E1:C9:4C:9A:EC:E5:5B:74:50:37:E1:A8:CE:B2:05:EB:8D:DD:92