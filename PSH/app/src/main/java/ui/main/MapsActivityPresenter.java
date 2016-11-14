package ui.main;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import model.House;


/**
 * Created by CFitzpatrick on 10/9/16.
 */

class MapsActivityPresenter {

    private final MapsActivityView view;

    public MapsActivityPresenter(MapsActivityView view){
        this.view = view;
    }

    public void findAndPlaceMarkers(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference houseRef = database.getReference("The_Houses");

        houseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                @SuppressWarnings("unchecked") HashMap <String, HashMap> response = (HashMap) dataSnapshot.getValue();
                Iterator it = response.entrySet().iterator();
                House currentHouse = new House();

                while (it.hasNext()){
                    @SuppressWarnings("unchecked") HashMap.Entry <String, HashMap> pair = (HashMap.Entry) it.next();
                    HashMap current = pair.getValue();

                    //currentHouse.address = pair.getKey();
                    currentHouse.address = "Address not listed";
                    if (current == null){
                        Log.d("current", currentHouse.address + " is null");
                        it.remove();
                        continue;
                    }
                    if (current.get("latitude") == null || current.get("longitude") == null || current.get("price") == null || current.get("bedrooms") == null){
                        Log.d("onDataChange", "One of the fields of " + currentHouse.address + " is null");
                        it.remove();
                        continue;
                    }

                    if (current.containsKey("address")) {
                        currentHouse.address = current.get("address").toString();
                    }
                    currentHouse.lat = Float.parseFloat(current.get("latitude").toString());
                    currentHouse.lon = Float.parseFloat(current.get("longitude").toString());
                    currentHouse.price = Integer.parseInt(current.get("price").toString());
                    currentHouse.count = Integer.parseInt(current.get("bedrooms").toString());

                    MarkerOptions toPlace = new MarkerOptions()
                            .position(new LatLng(currentHouse.lat, currentHouse.lon))
                            .title(currentHouse.address)
                            .snippet(currentHouse.count + " rooms, " + currentHouse.price + " per month");
                    view.placeMarker(toPlace);

                    it.remove();
                }

                Set <String> keys = response.keySet();
                response.get(keys.iterator());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MapsActivityPresenter", "onCancelled: " + databaseError);
            }
        });

        MarkerOptions toPlace = new MarkerOptions()
                .position(new LatLng(42.408, -71.129))
                .title("90 Conwell Ave, Apt. 2")
                .snippet("4 Bedrooms, $850/month");
        view.placeMarker(toPlace);
    }

}
