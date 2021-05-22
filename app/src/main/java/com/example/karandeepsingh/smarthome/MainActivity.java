package com.example.karandeepsingh.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    CardView card3,card2,card4,card1;
    TextView textViewRoom1,textViewRoom2,textViewTime,textViewUsername;
    Dialog changeNameDialog;
    String time;
    ImageView imageViewBg,imageViewBR;
    String uid;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);
        textViewRoom1=findViewById(R.id.textViewRoom1);
        textViewRoom2=findViewById(R.id.textViewRoom2);
        textViewTime=findViewById(R.id.textViewTime);
        textViewUsername=findViewById(R.id.userName);
        imageViewBg=findViewById(R.id.imageViewBG);
        imageViewBR=findViewById(R.id.bRImage);
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Name", 0); // 0 - for private mode
        String roomType1=pref.getString("RoomNo1","Room1");
        String roomType2=pref.getString("RoomNo2","Room2");

        textViewRoom1.setText(roomType1);
        textViewRoom2.setText(roomType2);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 4 && timeOfDay < 12){
            time="Good Morning";
            imageViewBg.setBackgroundResource(R.drawable.morning);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            time="Good Afternoon";
            imageViewBg.setBackgroundResource(R.drawable.afternoon);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            time="Good Evening";
            imageViewBg.setBackgroundResource(R.drawable.evening);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            time="Good Night";
            imageViewBg.setBackgroundResource(R.drawable.night);
        }else if(timeOfDay >= 0 && timeOfDay < 4){
            time="Good Night";
            imageViewBg.setBackgroundResource(R.drawable.night);
        }
        textViewTime.setText(time);




        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChargingModeActivity.class));
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ControlAppliances.class);

                //Pair<View, String> pair=Pair.create((View)card1,ViewCompat.getTransitionName(card1));
                Pair<View, String> pair1=Pair.create((View)textViewRoom1, ViewCompat.getTransitionName(textViewRoom1));
                Pair<View, String> pair2=Pair.create((View)imageViewBR,ViewCompat.getTransitionName(imageViewBR));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,pair1,pair2);
                    startActivity(intent,optionsCompat.toBundle());
            }
        });
        card3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changeNameDialog=new Dialog(MainActivity.this);
                changeNameDialog.setContentView(R.layout.room_layout);
                final EditText editTextName= changeNameDialog.findViewById(R.id.changeName);
                Button buttonSave= changeNameDialog.findViewById(R.id.saveButton);
                Button buttonCancel= changeNameDialog.findViewById(R.id.cancelButton);
                changeNameDialog.show();

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String roomName=editTextName.getText().toString();
                        if(TextUtils.isEmpty(roomName)){
                            editTextName.setError("Enter Room Name");
                            return;
                        }
                        textViewRoom1.setText(roomName);
                        sharedPrefSave(roomName,"RoomNo1");
                        changeNameDialog.dismiss();
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeNameDialog.dismiss();
                    }
                });
                return false;
            }
        });
        card4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changeNameDialog=new Dialog(MainActivity.this);
                changeNameDialog.setContentView(R.layout.room_layout);
                final EditText editTextName= changeNameDialog.findViewById(R.id.changeName);
                Button buttonSave= changeNameDialog.findViewById(R.id.saveButton);
                Button buttonCancel= changeNameDialog.findViewById(R.id.cancelButton);
                changeNameDialog.show();
                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String roomName=editTextName.getText().toString();
                        if(TextUtils.isEmpty(roomName)){
                            editTextName.setError("Enter Room Name");
                            return;
                        }
                        textViewRoom2.setText(roomName);
                        sharedPrefSave(roomName,"RoomNo2");
                        changeNameDialog.dismiss();
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeNameDialog.dismiss();
                    }
                });
                return false;
            }
        });




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                textViewUsername.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sharedPrefSave(String name,String room_type){
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("Name",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(room_type,name);
        editor.apply();
    }
}
