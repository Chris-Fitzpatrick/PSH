package ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import model.House;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class FindRegion extends FragmentActivity implements FindRegionView, OnMapReadyCallback{

    private FindRegionPresenter presenter;
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate FindRegion", "Very beginning of the FindRegion view");

        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setTitle("Place Markers")
                        .setMessage("Place markers on areas that you regularly visit on campus.")
                        .setPositiveButton("Get Started", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onDismiss();
                            }
                        });

        // Show the AlertDialog.
        AlertDialog alertDialog = alertDialogBuilder.show();
        super.onCreate(savedInstanceState);
        if (presenter == null){
            presenter = new FindRegionPresenter(this);
        }
        if (presenter.places == null){
            presenter.places = new ArrayList<LatLng>();
        }
        setContentView(R.layout.find_region);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);

    }

    private void onDismiss(){


    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                presenter.places.add(latLng);
                markerOptions.title("marker at " + latLng.latitude + " : " + latLng.longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }
        });
    }

    public void calculateClicked(View view){

        presenter.calculate();
    }

}
