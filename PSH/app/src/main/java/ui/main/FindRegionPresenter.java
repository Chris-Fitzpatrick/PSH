package ui.main;

import android.content.Context;
import android.os.AsyncTask;
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
import java.util.Collections;
import java.util.Comparator;
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



    public class HouseComparator implements Comparator<House>{

        @Override
        public int compare(House lhs, House rhs) {
            if (lhs.meters > rhs.meters){
                return 1;
            }
            if (lhs.meters == rhs.meters){
                return 0;
            }
            return -1;
        }
    }

    public void calculate() {

        checkForHouses();
        House currentHouse;
        Log.d("in presenter", "calculating " + houses.size() + " x " + places.size());

        for (int i = 0; i < houses.size(); i++){
            currentHouse = houses.get(i);
            float distance = 0;

            for (int j = 0; j < places.size(); j++){

                distance += distFrom(currentHouse.lat, currentHouse.lon, places.get(j).latitude, places.get(j).longitude);
            }

            houses.get(i).meters = distance;
        }
        for (int i = 0; i < houses.size(); i++){
            Log.d("before sort: ", "i: " + i + " is " + houses.get(i).meters);
        }
        Collections.sort(houses, new HouseComparator());
        for (int i = 0; i < houses.size(); i++){
            Log.d("after sort: ", "i: " + i + " is " + houses.get(i).meters);
        }

        place1through5();
    }

    void place1through5(){

        for (int i = 0; i < 5; i++){
            view.placeHouse(houses.get(i));
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
        else {
            new GetValues().execute();
        }

    }

    public void downloadValues() {
        new GetValues().execute();
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

    public class GetValues extends AsyncTask<Void, Void, Void> {


        public GetValues() {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

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

                        houses.add(currentHouse);
                        it.remove();
                    }
                    Set<String> keys = response.keySet();
                    response.get(keys.iterator());
                    found = true;
                    Log.d("async get houses: ", "returning");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("MapsActivityPresenter", "onCancelled: " + databaseError);
                }
            });

            return null;
        }

    }
}
