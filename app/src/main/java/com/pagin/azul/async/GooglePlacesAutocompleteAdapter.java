package com.pagin.azul.async;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.pagin.azul.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<ModelPlace> resultList = new ArrayList<>();
    public static ArrayList placeId = new ArrayList();
    private static final String LOG_TAG = "Google Places Autocomplete";
    private Context context;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public ModelPlace getItem(int index) {
        return (ModelPlace) resultList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_location_white, parent,
                false);
        TextView nameView = (TextView) rowView.findViewById(R.id.txt1);
        if (resultList.size() > 0) {
            nameView.setText(resultList.get(position).getPlaceName());
        }
//        phoneView.setText(contactMap.get("phone"));
//        typeView.setText(contactMap.get("type"));

        return rowView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                             FilterResults filterResults = new FilterResults();


                if (constraint != null && constraint.length()>=1) {
                    // Retrieve the autocomplete results.
                    ArrayList<ModelPlace> resultPlaces = autocomplete(constraint.toString(), context);
//                    placeId = autocomplete(constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = resultPlaces;
                    filterResults.count = resultPlaces.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                resultList.clear();
                if (results != null && results.count > 0) {
                    resultList.addAll((ArrayList<ModelPlace>) results.values);
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    public static ArrayList autocomplete(String input, Context context) {
        ArrayList<ModelPlace> resultList = null;
        ArrayList placeid1 = null;
        placeId.clear();
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String countryCode = ((TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE)).getNetworkCountryIso();
           // Log.e("countryCode"," RAVI "+countryCode);

            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+input+"&components=country:"+countryCode+"&radius=1000&types=establishment&language=eng&key="+IntentConstants.GOOGLE_API_KEY_PLACE);
            //StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place" + "/autocomplete" + "/json");
            //sb.append("?key=" + IntentConstants.GOOGLE_API_KEY_PLACE);//GOOGLE_API_KEY
//            sb.append("?key=" + context.getString(R.string.google_map_api_key));//GOOGLE_API_KEY

            //sb.append("&input=" + URLEncoder.encode(input, "utf8"));



            URL url = new URL(sb.toString());
            Log.e("urlPlayService", sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            // Log.e(LOG_TAG, "Error  API URL", e);
            return resultList;
        } catch (IOException e) {
            // Log.e(LOG_TAG, "Error Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            placeId = new ArrayList();
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            placeid1 = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                JSONObject jsonObject = predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting");
                String spotName = jsonObject.optString("main_text");
                String region = jsonObject.optString("secondary_text");
                placeId.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                resultList.add(new ModelPlace(predsJsonArray.getJSONObject(i).getString("place_id"),
                        predsJsonArray.getJSONObject(i).getString("description"), spotName, region));
            }

        } catch (JSONException e) {
            // Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public String getPlaceId(int pos) {

        return placeId.get(pos) + "";
    }
}