package ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class PostListing extends FragmentActivity implements PostListingView, OnMapReadyCallback {

    private PostListingPresenter presenter;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate PostListing", "Very beggining of the PostListing view");
        super.onCreate(savedInstanceState);

        if (presenter == null){
            presenter = new PostListingPresenter(this);
        }

        setContentView(R.layout.post_listing);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

}
