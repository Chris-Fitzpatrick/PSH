package ui.main;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import ui.main.R;


public class MapsActivity extends FragmentActivity implements MapsActivityView, OnMapReadyCallback {
    private MapsActivityPresenter presenter;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MapsActivity", "onCreate triggered");

        super.onCreate(savedInstanceState);

        if (presenter == null){
            presenter = new MapsActivityPresenter(this);
        }

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        presenter.findAndPlaceMarkers();

    }

    @Override
    public void placeMarker(MarkerOptions toPlace) {
        mMap.addMarker(toPlace);
    }
}
