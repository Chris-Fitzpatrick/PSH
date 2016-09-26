package com.lunaandchris.projstudenthousing.psh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.CharSequence;
import android.util.Log;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                setContentView(R.layout.activity_maps);
            }
        });

    }

}
