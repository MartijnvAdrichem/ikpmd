package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.marti.pocketbattle.highscores.HighscoreView;
import com.example.marti.pocketbattle.models.User;
import com.example.marti.pocketbattle.pokemon.PokemonList;
import com.example.marti.pocketbattle.pokemon.PokemonSelectBattleList;
import com.example.marti.pocketbattle.shop.ShopView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeScreenView extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView hsText;
    public static FirebaseUser currentUser;
    ImageButton shopButton;
    ImageButton battleButton;
    Button logoutButton;
    ImageButton profileButton;
    ImageButton highscoreButton;

    DatabaseReference userRef;
    public static User user;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_view);
        mAuth = FirebaseAuth.getInstance();
        hsText = (TextView) findViewById(R.id.hsText);
        shopButton = findViewById(R.id.shopButton);
        battleButton = findViewById(R.id.battleButton);
        logoutButton = findViewById(R.id.logoutButton);
        profileButton = findViewById(R.id.profileButton);
        currentUser = mAuth.getCurrentUser();
        battleButton = findViewById(R.id.battleButton);
        highscoreButton = findViewById(R.id.highscoreButton);


        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users/" + currentUser.getUid());
    }

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

         userRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot d : dataSnapshot.getChildren()){
                      user = d.getValue(User.class);
                 }
                 hsText.setText("Welcome, " + user.username);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
        }

        shopButton.setOnClickListener(e -> {
            startActivity(new Intent(HomeScreenView.this, ShopView.class));

        });

        battleButton.setOnClickListener(e -> {
            startActivity(new Intent(HomeScreenView.this, PokemonSelectBattleList.class));

        });

        logoutButton.setOnClickListener(e ->{
            mAuth.signOut();
            startActivity(new Intent(HomeScreenView.this, LoginView.class));
        });

        profileButton.setOnClickListener(e ->{
            startActivity(new Intent(HomeScreenView.this, ProfileView.class));
        });

        highscoreButton.setOnClickListener(e ->{
            startActivity(new Intent(HomeScreenView.this, HighscoreView.class));
        });
    }

    public static void updateUser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("users/" + HomeScreenView.currentUser.getUid() + "/user");
        userRef.removeValue();
        userRef.setValue(HomeScreenView.user);
    }
}
