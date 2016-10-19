package ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class PostListing extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate PostListing", "Very beggining of the PostListing view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_listing);
    }
}
