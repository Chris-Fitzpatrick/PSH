package ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Listing;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class PostListing extends FragmentActivity implements PostListingView {

    // implements PostListingView, OnMapReadyCallback (when maps implemented)
    private PostListingPresenter presenter;

    Listing postListing = new Listing();

    // private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate PostListing", "Very beggining of the PostListing view");
        super.onCreate(savedInstanceState);

        if (presenter == null) {
            presenter = new PostListingPresenter(this);
        }

        presenter.extractAddresses();
        setContentView(R.layout.post_listing);

        // get rid of search address all together upon finishing
        View searchAddress = findViewById(R.id.searchAddress);
        searchAddress.setVisibility(View.GONE);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();

        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment
                .setBoundsBias(new LatLngBounds(
                    new LatLng(42.372872, -71.149579),
                    new LatLng(42.425860, -71.075936)));


        final EditText editText = (EditText) findViewById(R.id.searchAddress);
        final EditText rentText = (EditText) findViewById(R.id.rent);
        final EditText bedsText = (EditText) findViewById(R.id.beds);
        final EditText latText = (EditText) findViewById(R.id.lattitude);
        final EditText longText = (EditText) findViewById(R.id.longitude);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                String inputAddress = place.getName().toString();
                String modAddress = inputAddress.replaceAll("\\W", "");
                modAddress = modAddress.toLowerCase();

                boolean addressExists = false;

                for (int i = 0; i < (presenter.numHouses); i++) {
                    //TODO: rewrite duplicate check logic
                    String currentAddress = presenter.addressArray[i].replaceAll("\\W", "");
                    currentAddress = currentAddress.toLowerCase();

                    if (modAddress.equals(currentAddress)) {
                        Toast.makeText(getApplicationContext(),
                                presenter.addressArray[i] + " is already a listing!",
                                Toast.LENGTH_SHORT).show();
                        addressExists = true;
                        break;
                    }

                }

                if (!(addressExists)) {
                    Toast.makeText(getApplicationContext(),
                            //editText.getText(),
                            inputAddress + " is not a listing, you're good to go!",
                            Toast.LENGTH_SHORT).show();
                    View lattitude = findViewById(R.id.lattitude);
                    lattitude.setVisibility(View.VISIBLE);
                    View longitude = findViewById(R.id.longitude);
                    longitude.setVisibility(View.VISIBLE);
                    View button5 = findViewById(R.id.button5);
                    button5.setVisibility(View.VISIBLE);

                    String latLong = place.getLatLng().toString().replace("lat/lng: (","").replace(")", "");

                    String[] latlon =  latLong.split(",");
                    String lat = latlon[0];
                    String lon = latlon[1];

                    latText.setText(lat);
                    longText.setText(lon);

                    postListing.address = inputAddress;

                }

            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),
                        ("We're sorry, an error occurred: " + status),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //MapFragment mapFragment = (MapFragment) getFragmentManager()
        //      .findFragmentById(R.id.map3);
        //mapFragment.getMapAsync(this);

        final Button submitButton = (Button) findViewById(R.id.button5);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                // convert Edit Text to Integer for all values.

                postListing.bedrooms= bedsText.getText().toString().replaceAll("\\W", "");
                postListing.price = rentText.getText().toString().replaceAll("\\W", "");
                postListing.latitude = latText.getText().toString();
                postListing.longitude = longText.getText().toString();


                if (!(postListing.address.isEmpty())) {
                    if (!(postListing.bedrooms.isEmpty())) {
                        if (!(postListing.price.isEmpty())) {
                            if (!(postListing.latitude.isEmpty())) {
                                if (!(postListing.longitude.isEmpty())) {
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("The_Houses");
                                    mDatabase.push().setValue(postListing);

                                    Intent goToMainIntent = new Intent(PostListing.this, MainActivity.class);
                                    startActivity(goToMainIntent);

                                    Toast.makeText(getApplicationContext(),
                                            "Listing successfully posted",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }

            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //@Override
    // public void onMapReady(GoogleMap map) {
    //   mMap = map;
    // }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PostListing Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //   client.connect();
        // AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();
    }
}
