package ui.main;
import android.util.Log;

import com.google.android.gms.appinvite.AppInviteInvitation;
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

public class MapsActivityPresenter {

    private MapsActivityView view;

    public MapsActivityPresenter(MapsActivityView view){
        this.view = view;
    }

    public void findAndPlaceMarkers(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference houseRef = database.getReference("The_Houses");

        houseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("Single Value Listen ***", "got data!");
                HashMap <String, HashMap> response = (HashMap) dataSnapshot.getValue();
                Iterator it = response.entrySet().iterator();
                House currentHouse = new House();

                while (it.hasNext()){
                    HashMap.Entry <String, HashMap> pair = (HashMap.Entry) it.next();
                    HashMap current = pair.getValue();

                    currentHouse.address = pair.getKey();
                    String latString = current.get("latitude").toString();
                    String lonString = current.get("longitude").toString();
                    String priceString = current.get("price").toString();
                    String countString = current.get("bedrooms").toString();

                    currentHouse.lat = Float.parseFloat(latString);
                    currentHouse.lon = Float.parseFloat(lonString);
                    currentHouse.price = Integer.parseInt(priceString);
                    currentHouse.count = Integer.parseInt(countString);

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

            }
        });

        MarkerOptions toPlace = new MarkerOptions()
                .position(new LatLng(42.408, -71.129))
                .title("90 Conwell Ave, Apt. 2")
                .snippet("4 Bedrooms, $850/month");
        view.placeMarker(toPlace);
    }

}
