package com.example.karandeepsingh.smarthome;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.NotificationCompat;

import static com.example.karandeepsingh.smarthome.AppChannel.CHANNEL_ID;

public class MyService extends Service {
    int mProgressStatus;
    int inputPercentage;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Service startedReceiver", Toast.LENGTH_LONG).show();

            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);

            float percentage = level/ (float) scale;
            mProgressStatus = (int)((percentage)*100);

            Toast.makeText(context, "Battery"+mProgressStatus, Toast.LENGTH_LONG).show();
            if(mProgressStatus>=inputPercentage){
                FirebaseDatabase.getInstance().getReference().child("S4").setValue(0);
                stopSelf();
            }
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        inputPercentage = Integer.parseInt(intent.getStringExtra("bPer"));
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        Intent notificationIntent= new Intent(this,ChargingModeActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Charging Service")
                .setContentText("The service is running in Backgroung")
                .setSmallIcon(R.drawable.beeer)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, iFilter);
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}
