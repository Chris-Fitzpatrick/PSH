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
                while (it.hasNext()){
                    HashMap.Entry <String, HashMap> pair = (HashMap.Entry) it.next();
                    Log.d( "inner sing value loop", pair.getKey() + " = " + pair.getValue());

                    HashMap current = pair.getValue();
               //     Object thePrice = (Object)current.get(1);
               //     Object lat = (Object)current.get("lat");

                    String var0 = current.get("lat").toString();
                    String var1 = current.get("lon").toString();
                    String var2 = current.get("price").toString();
                    String var3 = current.get("rooms").toString();

                    Float lat = Float.parseFloat(var0);
                    Float lon = Float.parseFloat(var1);
                    int price = Integer.parseInt(var2);
                    int rooms = Integer.parseInt(var3);

                    int sum = rooms + price;

                    Log.d ("lat: ", var0);
                    Log.d ("lon: ", var1);
                    Log.d ("price: ", var2);
                    Log.d ("rooms: ", var3);

                    Log.d ("sum: ", "sum is " + sum);


                    MarkerOptions toPlace = new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title(pair.getKey())
                            .snippet(rooms + " rooms, " + price + " per month");
                    view.placeMarker(toPlace);

                  //  Log.d ("hail mary", current.get("price").toString());

                    //Log.d( "current ", current);
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



/*    FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference countRef = database.getReference("housecount");
        DatabaseReference listingRef = database.getReference("houses");

        countRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long theValue = (Long)snapshot.getValue();
                countRef.setValue(theValue + 1);
                Log.d("oncreate addValue:", "the value is " + theValue);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("oncreate fail", "The read failed: " + firebaseError.getMessage());
            }
        }); */

// myRef.setValue("Hello, World! 3");
// String message = countRef.toString();
// Log.d("end of oncreate", message);