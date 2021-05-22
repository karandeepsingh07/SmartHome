package com.example.karandeepsingh.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitorApplianceS4 extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView textViewHour,textViewMinute,textViewSecond;
    GifImageView gifImageView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_appliance_s4);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        textViewHour = findViewById(R.id.textViewHour);
        textViewMinute = findViewById(R.id.textViewMinute);
        textViewSecond = findViewById(R.id.textViewSecond);
        gifImageView = findViewById(R.id.chargerGIF);
        imageView = findViewById(R.id.chargerImage);

        reference.child("S4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(Integer.parseInt(dataSnapshot.getValue().toString())==1){
                    gifImageView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }
                else{
                    gifImageView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Hours3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewHour.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Minutes3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewMinute.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Seconds3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String x= dataSnapshot.getValue().toString();
                if(x.length()>1)
                    textViewSecond.setText(dataSnapshot.getValue().toString().substring(0,2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
