package ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
