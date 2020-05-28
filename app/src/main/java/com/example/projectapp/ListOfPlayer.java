package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOfPlayer extends AppCompatActivity {
    ListView playerlist;
    Button createroom;

    List<String> roomsList;

    String playerName= "";
    String roomName= "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_player);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName = preferences.getString("playerName","");
        roomName=playerName;

        playerlist = findViewById(R.id.playerlist);
        createroom= findViewById(R.id.createroom);
        //all exiting available rooms
        roomsList= new ArrayList<>();

        createroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createroom.setText("CREATING ROOM");
                createroom.setEnabled(false);
                roomName = playerName;
                roomRef = database.getReference("rooms/"+ roomName+"/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);

            }
        });

        playerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //join existing room and add yourself at player2
                roomName = roomsList.get(position);
                roomRef=database.getReference("rooms/"+roomName+"/player2");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        addRoomsEventListener();
    }
    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //join the room
                createroom.setText("Create Room(Join)");
                createroom.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(),PlayActivity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                createroom.setText("CREATE ROOM");
                createroom.setEnabled(true);
                Toast.makeText(ListOfPlayer.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addRoomsEventListener(){
        roomsRef=database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    roomsList.clear();
                    Iterable<DataSnapshot> rooms= dataSnapshot.getChildren();
                    for(DataSnapshot snapshot: rooms){
                        roomsList.add(snapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ListOfPlayer.this, android.R.layout.simple_list_item_1,roomsList);
                        playerlist.setAdapter(adapter);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error - nothing
            }
        });

    }
}
