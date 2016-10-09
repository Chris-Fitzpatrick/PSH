package com.lunaandchris2.projstudenthousing.projectstudenthousing.ui.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lunaandchris2.projstudenthousing.projectstudenthousing.R;
import com.lunaandchris2.projstudenthousing.projectstudenthousing.ui.ui.maps.MapsActivity;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureView();
    }

    private void configureView(){
        final Button firstButton = (Button) findViewById(R.id.button1);
        firstButton.setOnClickListener(new View.OnClickListener(){
             @Override
                public void onClick(View view){
                 //MapsActivity.launchActivity(MainActivity.this);
                 Intent goToMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                 startActivity(goToMapIntent);
             }
        });
    }
}
