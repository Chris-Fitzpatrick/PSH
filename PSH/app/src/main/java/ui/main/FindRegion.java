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

        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setTitle("Place Markers")
                        .setMessage("Place markers on areas that you regularly visit on campus. Then press 'Calculate' to see 5 houses recommended for you.")
                        .setPositiveButton("Get Started", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onDismiss();
                            }
                        });

        alertDialogBuilder.show();
        super.onCreate(savedInstanceState);
        if (presenter == null){
            presenter = new FindRegionPresenter(this);
        }
        setContentView(R.layout.find_region);

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

            //get frequent location for user
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                presenter.places.add(latLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }
        });
    }

    public void calculateClicked(View view){
        presenter.calculate();
    }


    public void placeHouse(House toAdd){

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(toAdd.lat, toAdd.lon);
        markerOptions.position(latLng);
        presenter.places.add(latLng);
        markerOptions.title(toAdd.address);
        markerOptions.snippet(toAdd.count + " rooms, " + toAdd.price + " per month");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void clearMarkers() {
        //remove all markers
        mMap.clear();
        //place the locations that the user frequently visits
        for (int i = 0; i < presenter.places.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(presenter.places.get(i).latitude, presenter.places.get(i).longitude));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(markerOptions);
        }
    }
}
