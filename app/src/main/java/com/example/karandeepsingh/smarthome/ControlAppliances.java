package com.example.karandeepsingh.smarthome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ControlAppliances extends AppCompatActivity {

    Switch switchAC, switchFan, switchRefrigerator, switchLight;
    FirebaseDatabase database;
    DatabaseReference reference;
    String s1="",s2="",s3="",s4="";
    TextView textViewTemperature,textViewHumidity,textViewConsumption;
    CardView cardAc;
    Double totalTime=0.0;
    int x=50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_appliances);


        init();

        checkButtonAC();
        checkButtonFan();
        checkButtonLight();
        checkButtonRef();
        totalTime();

       // switchTrigger();

        reference.child("Temp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewTemperature.setText(dataSnapshot.getValue().toString()+"°C");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Humidity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textViewHumidity.setText(dataSnapshot.getValue().toString()+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void init() {
        switchAC = findViewById(R.id.switchAC);
        switchFan = findViewById(R.id.switchFan);
        switchRefrigerator = findViewById(R.id.switchRefrigerator);
        switchLight = findViewById(R.id.switchLight);
        cardAc=findViewById(R.id.cardAC);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        textViewConsumption=findViewById(R.id.textViewConsumption);

        textViewTemperature=findViewById(R.id.textViewTemp);
        textViewHumidity=findViewById(R.id.textViewHumidity);
    }

    public void checkButtonAC(){
        reference.child("S1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                s1= dataSnapshot.getValue().toString();
                if(s1.equals("1")){
                    switchAC.setChecked(true);
                }else{
                    switchAC.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkButtonFan(){
        reference.child("S2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                s2= dataSnapshot.getValue().toString();
                if(s2.equals("1")){
                    switchFan.setChecked(true);
                }else{
                    switchFan.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkButtonLight(){
        reference.child("S3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                s3= dataSnapshot.getValue().toString();
                if(s3.equals("1")){
                    switchLight.setChecked(true);
                }else{
                    switchLight.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkButtonRef(){
        reference.child("S4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                s4= dataSnapshot.getValue().toString();
                if(s4.equals("1")){
                    switchRefrigerator.setChecked(true);
                }else{
                    switchRefrigerator.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void switchTriggerAC(View view) {
        if (switchAC.isChecked()) {
            reference.child("S1").setValue(1);
            //switchAC.setChecked(false);
        } else {
            reference.child("S1").setValue(0);
            //switchAC.setChecked(true);
        }
    }

    public void switchTriggerFan(View view) {
        if (switchFan.isChecked()) {
            reference.child("S2").setValue(1);
           // switchFan.setChecked(false);
        } else {
            reference.child("S2").setValue(0);
            //switchFan.setChecked(true);
        }
    }

    public void switchTriggerLight(View view) {
        if (switchLight.isChecked()) {
            reference.child("S3").setValue(1);
            //switchLight.setChecked(false);
        } else {
            reference.child("S3").setValue(0);
           // switchLight.setChecked(true);
        }
    }

    public void switchTriggerRef(View view) {
        if (switchRefrigerator.isChecked()) {
            reference.child("S4").setValue(1);
            //switchRefrigerator.setChecked(false);
        } else {
            reference.child("S4").setValue(0);
            //switchRefrigerator.setChecked(true);
        }
    }



    public void switchTrigger(){
        switchAC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    reference.child("S1").setValue(1);
                }
                else{
                    reference.child("S1").setValue(0);
                }
            }
        });
        switchFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    reference.child("S2").setValue(1);
                }
                else{
                    reference.child("S2").setValue(0);
                }
            }
        });
        switchLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    reference.child("S3").setValue(1);
                }
                else{
                    reference.child("S3").setValue(0);
                }
            }
        });
        switchRefrigerator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    reference.child("S4").setValue(1);
                }
                else{
                    reference.child("S4").setValue(0);
                }
            }
        });

    }

    public void openLight(View view){
        startActivity(new Intent(ControlAppliances.this,MonitorAppliances.class));
    }

    public void openFan(View view){
        startActivity(new Intent(ControlAppliances.this,MoniterApplianceS2.class));
    }

    public void openSwitch1(View view){
        startActivity(new Intent(ControlAppliances.this,MoniterApplianceS1.class));
    }
    public void openSwitch2(View view){
        startActivity(new Intent(ControlAppliances.this,MonitorApplianceS4.class));
    }

    public void totalTime(){
        reference.child("Hours1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Minutes1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Seconds1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString());
                Double consumption=(totalTime/3600000)*x;
                String result = String.format("%.2f", consumption);
                textViewConsumption.setText(result+" Kwh");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });reference.child("Hours2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Minutes2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Seconds2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString());
                Double consumption=(totalTime/3600000)*x;
                String result = String.format("%.2f", consumption);
                textViewConsumption.setText(result+" Kwh");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });reference.child("Hours3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Minutes3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Seconds3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString());
                Double consumption=(totalTime/3600000)*x;
                String result = String.format("%.2f", consumption);
                textViewConsumption.setText(result+" Kwh");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });reference.child("Hours4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Minutes4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString())*60;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Seconds4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalTime+=Double.parseDouble(dataSnapshot.getValue().toString());
                Double consumption=(totalTime/3600000)*x;
                String result = String.format("%.2f", consumption);
                textViewConsumption.setText(result+" Kwh");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

}
