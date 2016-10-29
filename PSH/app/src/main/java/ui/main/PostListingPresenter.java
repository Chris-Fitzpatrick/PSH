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

import model.Addresses;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class PostListingPresenter {

    private PostListingView view;

    public PostListingPresenter(PostListingView view){
        this.view = view;
    }

    public String[] addressArray = new String[100];

    public int numHouses = 0;

    public void extractAddresses() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference addressRef = database.getReference("The_Houses");

        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap <String, HashMap> response = (HashMap) dataSnapshot.getValue();
                Iterator it = response.entrySet().iterator();

                // figure out variable length of array later,
                // as well as incorporate Address data model.
                //String[] addressArray = new String[100];


                while (it.hasNext()){
                    HashMap.Entry <String, HashMap> pair = (HashMap.Entry) it.next();
                    addressArray[numHouses] = pair.getKey();
                    numHouses++;

                }
                Set<String> keys = response.keySet();
                response.get(keys.iterator());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MapsActivityPresenter", "onCancelled: " + databaseError);
            }
        });

    }

}
