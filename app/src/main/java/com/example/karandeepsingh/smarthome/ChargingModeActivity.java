package com.example.karandeepsingh.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChargingModeActivity extends AppCompatActivity {

    EditText editTextBattery;
    Button buttonOn,buttonOff;
    ImageView batteryImage;
    Drawable frameAnimation;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_mode);
        buttonOn=findViewById(R.id.butonOn);
        buttonOff=findViewById(R.id.buttonOff);
        editTextBattery=findViewById(R.id.batteryPer);
        batteryImage=findViewById(R.id.batterImage);


        batteryImage.setBackgroundResource(R.drawable.battery_drawable);
        frameAnimation = batteryImage.getBackground();

        if(isMyServiceRunning(MyService.class)){
            if(frameAnimation instanceof Animatable)
            {
                ( (Animatable)frameAnimation).start();
            }
            editTextBattery.setVisibility(View.INVISIBLE);
        }

        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // batteryImage.setVisibility(View.VISIBLE);
                if(editTextBattery.getText().toString().isEmpty()){
                    editTextBattery.setError("Enter Battery Percentage!");
                    return;
                }
                if(frameAnimation instanceof Animatable)
                {
                    ( (Animatable)frameAnimation).start();
                }
                Intent serviceIntent=new Intent(ChargingModeActivity.this,MyService.class);
                serviceIntent.putExtra("bPer",editTextBattery.getText().toString());
                startService(serviceIntent);
                editTextBattery.setVisibility(View.INVISIBLE);
            }
        });
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Animatable)frameAnimation).isRunning() )
                {
                    ((Animatable)frameAnimation).stop();
                }
                stopService(new Intent(ChargingModeActivity.this,MyService.class));
                editTextBattery.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
