package ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.lang.CharSequence;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import ui.main.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate main", "Very beggining of the app");
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        final Button firstButton = (Button) findViewById(R.id.button1);
        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent goToMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(goToMapIntent);
            }
        });

        final Button secondButton = (Button) findViewById(R.id.button2);
        secondButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent goToFindRegion = new Intent(MainActivity.this, FindRegion.class);
                startActivity(goToFindRegion);
            }
        });

        final Button thirdButton = (Button) findViewById(R.id.button3);
        thirdButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent goToPostListing = new Intent(MainActivity.this, PostListing.class);
                startActivity(goToPostListing);
            }
        });
    }

}
