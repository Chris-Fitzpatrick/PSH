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


    private final FindRegionView view;
    boolean found = false;
    protected ArrayList<House> houses = new ArrayList<House>();
    protected ArrayList<LatLng> places;


    public FindRegionPresenter (FindRegionView view){
        this.view = view;
        places = new ArrayList<LatLng>();
        downloadHouseData();
    }


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
        for (int i = 0; i < houses.size(); i++){
            currentHouse = houses.get(i);
            float distance = 0;

            for (int j = 0; j < places.size(); j++){
                distance += distFrom(currentHouse.lat, currentHouse.lon, places.get(j).latitude, places.get(j).longitude);
            }
            houses.get(i).meters = distance;
        }

        Collections.sort(houses, new HouseComparator());
        place1through5();
    }

    void place1through5(){
        view.clearMarkers();
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

    public void downloadHouseData() {
        new GetValues().execute();
    }


    void walkingDirections(LatLng start, LatLng dest){

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+start.latitude+","+start.longitude+"&destinations="+dest.latitude+","+start.longitude+"&mode=walking&language=EN&key=AIzaSyBFSv3d8xFYoL8S8ghfODkbZT8aN4ORo1E";

        RequestQueue queue = Volley.newRequestQueue((Context)view);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                    while (it.hasNext()){
                        @SuppressWarnings("unchecked") HashMap.Entry <String, HashMap> pair = (HashMap.Entry) it.next();
                        HashMap current = pair.getValue();
                        House currentHouse = new House();
                        currentHouse.address = pair.getKey();
                        Log.d("current house: ", currentHouse.address);
                        if (current == null){
                            Log.d("current", currentHouse.address + " is null");
                            it.remove();
                            continue;
                        }
                        currentHouse.address = pair.getKey();
                        if (current.get("latitude") == null || current.get("longitude") == null || current.get("price") == null || current.get("bedrooms") == null){
                            Log.d("onDataChange", "One of the fields of " + currentHouse.address + " is null");
                            it.remove();
                            continue;
                        }
                        Log.d("current.get(lat)", current.get("latitude").toString());
                        currentHouse.lat = Float.parseFloat(current.get("latitude").toString());
                        currentHouse.lon = Float.parseFloat(current.get("longitude").toString());
                        currentHouse.price = Integer.parseInt(current.get("price").toString());
                        currentHouse.count = Integer.parseInt(current.get("bedrooms").toString());

                        houses.add(currentHouse);
                        it.remove();
                    }
                    Set<String> keys = response.keySet();
                    response.get(keys.iterator());
                    found = true;
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
