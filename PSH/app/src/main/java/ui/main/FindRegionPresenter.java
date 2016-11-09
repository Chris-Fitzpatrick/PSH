package ui.main;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.House;


/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class FindRegionPresenter {

    boolean found = false;
    protected ArrayList<House> houses = new ArrayList<House>();
    protected ArrayList<LatLng> places;

    public void calculate() {

        checkForHouses();
        House currentHouse;
        Log.d("in presenter", "calculating " + houses.size() + " x " + places.size());

        for (int i = 0; i < houses.size(); i++){
            currentHouse = houses.get(i);
            Log.d("Calculating for: ", currentHouse.address + " cost: " + currentHouse.price);
            float distance = 0;

            for (int j = 0; j < places.size(); j++){

                distance += distFrom(currentHouse.lat, currentHouse.lon, places.get(j).latitude, places.get(j).longitude);
            }
            Log.d("found it to be ", distance + " away \n");

        }
    }

    //from http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


    private void checkForHouses() {
        if (found){
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference houseRef = database.getReference("The_Houses");

        houseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                @SuppressWarnings("unchecked") HashMap <String, HashMap> response = (HashMap) dataSnapshot.getValue();
                Iterator it = response.entrySet().iterator();
                //House currentHouse = new House();

                while (it.hasNext()){
                    @SuppressWarnings("unchecked") HashMap.Entry <String, HashMap> pair = (HashMap.Entry) it.next();
                    HashMap current = pair.getValue();
                    House currentHouse = new House();
                    currentHouse.address = pair.getKey();
                    String latString = current.get("latitude").toString();
                    String lonString = current.get("longitude").toString();
                    String priceString = current.get("price").toString();
                    String countString = current.get("bedrooms").toString();

                    currentHouse.lat = Float.parseFloat(latString);
                    currentHouse.lon = Float.parseFloat(lonString);
                    currentHouse.price = Integer.parseInt(priceString);
                    currentHouse.count = Integer.parseInt(countString);

                    Log.d("print: ", "adding: " + currentHouse.address);
                    houses.add(currentHouse);
                    it.remove();
                }
                Log.d("print: ", "done getting ");
                Set<String> keys = response.keySet();
                response.get(keys.iterator());
                found = true;
                Log.d("at: ", "address 5: " + houses.get(5).address + " and " + houses.get(3).address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MapsActivityPresenter", "onCancelled: " + databaseError);
            }
        });

    }

    public class latLong{
        double lat;
        double lon;
    }

    private final FindRegionView view;
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
