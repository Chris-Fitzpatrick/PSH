package ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class FindRegion extends FragmentActivity implements FindRegionView, OnMapReadyCallback {

    private FindRegionPresenter presenter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate FindRegion", "Very beggining of the FindRegion view");
        super.onCreate(savedInstanceState);
        if (presenter == null){
            presenter = new FindRegionPresenter(this);
        }

        setContentView(R.layout.find_region);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

        Log.d("findregion.java", "about to call the example method");
        presenter.example();
        Log.d("findregion.java", "back from the example method");

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }
}
