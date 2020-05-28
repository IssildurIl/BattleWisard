package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SharedMemory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    EditText LogNick;
    Button ButtonLog;

    String playerName="";

    FirebaseDatabase database;
    DatabaseReference playerRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        LogNick = findViewById(R.id.LogNick);
        ButtonLog= findViewById(R.id.ButtonLog);

        database = FirebaseDatabase.getInstance();
        FontTexts();
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName=preferences.getString("Player name","");
        if (!playerName.equals("")){
            playerRef = database.getReference("players/"+ playerName);
            addEventListener();
            playerRef.setValue("");
        }
        ButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = LogNick.getText().toString();
                LogNick.setText("");
                    if(!playerName.equals("")){
                        ButtonLog.setText("LOGGING IN");
                        ButtonLog.setEnabled(false);
                        playerRef=database.getReference("players/"+ playerName);
                        addEventListener();
                        playerRef.setValue("");
                    }
            }
        });
    }
    private void addEventListener(){
        //read from database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //success-continue after saving playername
                if(!playerName.equals("")){
                    SharedPreferences preferences= getSharedPreferences("PREFS",0);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("playerName",playerName);
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(), ListOfPlayer.class);
                    startActivity(i);
                    /*startActivity(new Intent(getApplicationContext(), ListOfPlayer.class));
                    finish();*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error
                ButtonLog.setText("LOG IN");
                ButtonLog.setEnabled(true);
                Toast.makeText(LogInActivity.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void FontTexts(){
        final TextView start = (TextView)findViewById(R.id.LogNick);
        start.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView quick = (TextView)findViewById(R.id.ButtonLog);
        quick.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
    }
}
