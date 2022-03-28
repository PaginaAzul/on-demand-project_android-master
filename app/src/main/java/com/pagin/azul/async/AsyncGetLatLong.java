package com.pagin.azul.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AsyncGetLatLong extends AsyncTask<Void, Void, String> {
    public Context context;
    private String placeId;
    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    OnLocationSelect listener;

    public AsyncGetLatLong(Context context, String placeId, OnLocationSelect listener) {
        this.context = context;
        this.placeId = placeId;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String baseUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="
                    + placeId + "&key=" + IntentConstants.GOOGLE_API_KEY_PLACE;
//            String baseUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="
//                    + placeId + "&key=" + context.getString(R.string.google_map_api_key);
            URL url = new URL(baseUrl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("Listener", "Error processing Places API URL", e);
            // return resultList;
        } catch (IOException e) {
            Log.e("Listener", "Error connecting to Places API", e);
            // return resultList;
        } catch (Exception e) {
            Log.e("Listener", "Error processing Places API URL", e);
            // return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
       // String s=jsonResults.toString();
        return jsonResults.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Log.e("result", result);
            JSONObject objMin;
            try {
                objMin = new JSONObject(result);
                JSONObject objRes = objMin.getJSONObject("result");
                Log.e("DATA", objRes.toString());
                JSONArray jArr = objRes.getJSONArray("address_components");
                JSONObject jObInnerCity = jArr.getJSONObject(0);
               // JSONObject city=jArr.getJSONObject(1);
               // String shortNameCity=city.getString("short_name");
                JSONObject jObInnerState;
                JSONObject jObInnerCountry;
                JSONObject jObInnerPin;

                JSONObject objGeo = objRes.getJSONObject("geometry");
                JSONObject objLoc = objGeo.getJSONObject("location");
                Double longitude = objLoc.getDouble("lng");
                Double latitude = objLoc.getDouble("lat");
                //txtResult.setText(item.getPlaceName());
               // CommonUtilities.hideSoftInput((Activity) context);
                //Toast.makeText(context, item.getPlaceName(), 1000).show();
                //	dialog.dismiss();
                listener.onLocationSelect(String.valueOf(latitude), String.valueOf(longitude), "", "", "", "");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}