package com.example.marti.pocketbattle.highscores;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HighscoreView extends AppCompatActivity {

    DatabaseReference usersRef;
    List<User> users = new ArrayList<>();
    FirebaseDatabase db;
    private FirebaseAuth mAuth;

    public static User user;

    ListView scoresList;
    private HighscoreAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_view);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usersRef = db.getReference("users");

        scoresList = findViewById(R.id.scoresList);

    }

    public void onStart(){
        super.onStart();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    user = d.child("user").getValue(User.class);
                    users.add(user);
                    Collections.sort(users, user.UsrSorting);
                }
                mAdapter = new HighscoreAdapter(HighscoreView.this, 0, users);
                scoresList.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}






