package ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by CFitzpatrick on 10/19/16.
 */

public class FindRegion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate FindRegion", "Very beggining of the FindRegion view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_region);
    }
}
