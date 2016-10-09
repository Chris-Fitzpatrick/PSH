package com.lunaandchris.projstudenthousing.psh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.CharSequence;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate main", "Very beggining of the app");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int count = 0;

        final TextView firstTextView = (TextView) findViewById(R.id.textview1);

        final Button firstButton = (Button) findViewById(R.id.button1);
        firstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String msgString = getResources().getString(R.string.button1msg);
                CharSequence charSeq = firstTextView.getText();
                String theString = charSeq.toString();
                String firstString = "Hello Galaxy!";
                String secondString = "hellow orld";

                Intent goToMapIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(goToMapIntent);
                //setContentView(R.layout.activity_maps);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference countRef = database.getReference("housecount");
        DatabaseReference listingRef = database.getReference("houses");

        countRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long theValue = (Long)snapshot.getValue();
                countRef.setValue(theValue + 1);
                Log.d("oncreate addValue:", "the value is " + theValue);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("oncreate fail", "The read failed: " + firebaseError.getMessage());
            }
        });

       // myRef.setValue("Hello, World! 3");
        String message = countRef.toString();
        Log.d("end of oncreate", message);
    }

}
