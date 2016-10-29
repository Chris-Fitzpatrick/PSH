package ui.main;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import org.json.*;

import java.util.List;


/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class FindRegionPresenter {

    public class latLong{
        double lat;
        double lon;
    }

    private FindRegionView view;
    private List<latLong> AreasOfInterest;

    public FindRegionPresenter (FindRegionView view){
        this.view = view;
    }



    void addLocation(double lat, double lon){
        latLong toAdd = new latLong();
        toAdd.lat = lat;
        toAdd.lon = lon;
        AreasOfInterest.add(toAdd);
    }

    void example(){
        Log.d("ex", "in the example method");

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=42.403685,-71.120482&destinations=42.401922,-71.116358&mode=walking&language=EN&key=AIzaSyBFSv3d8xFYoL8S8ghfODkbZT8aN4ORo1E";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue((Context)view);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("in success", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            /*
                            JSONArray array = obj.getJSONArray("rows");
                            Log.d("printing array: ", array.toString());
                            JSONObject obj2 = array.getJSONObject(0);
                            Log.d("printing obj2: ", obj2.toString());
                            JSONArray obj3 = obj2.getJSONArray("elements");
                            Log.d("printing obj3: ", obj3.toString());
                            JSONObject obj4 = obj3.getJSONObject(0);
                            Log.d("printing obj4: ", obj4.toString());
                            JSONObject obj5 = obj4.getJSONObject("duration");
                            Log.d("printing obj5: ", obj5.toString());
                            int value = obj5.getInt("value");
                            Log.d("printing value: ", Integer.toString(value));
                            */

                            int walkTime = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getInt("value");
                            Log.d("printing walkTime: " , Integer.toString(walkTime));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error in request", String.valueOf(error));
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



}
