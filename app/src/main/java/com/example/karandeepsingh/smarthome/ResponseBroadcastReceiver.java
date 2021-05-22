package com.example.karandeepsingh.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class ResponseBroadcastReceiver extends BroadcastReceiver {

    int mProgressStatus;
    public void onReceive(Context context, Intent intent) {
        //get the broadcast message
        Toast.makeText(context, "Service startedReceiver", Toast.LENGTH_LONG).show();

        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

        float percentage = level/ (float) scale;
        mProgressStatus = (int)((percentage)*100);

        Toast.makeText(context, "Battery"+mProgressStatus, Toast.LENGTH_LONG).show();
        if(mProgressStatus<99){
            FirebaseDatabase.getInstance().getReference().child("S1").setValue(0);
        }
    }

}
