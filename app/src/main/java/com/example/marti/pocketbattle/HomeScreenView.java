package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.marti.pocketbattle.pokemon.PokemonList;
import com.example.marti.pocketbattle.pokemon.PokemonSelectBattleList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class HomeScreenView extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView hsText;
    public static FirebaseUser currentUser;
    ImageButton listViewButton;
    ImageButton battleButton;
    Button logoutButton;

    DatabaseReference userRef;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_view);
        mAuth = FirebaseAuth.getInstance();
        hsText = (TextView) findViewById(R.id.hsText);
        listViewButton = findViewById(R.id.pokemonButton);
        battleButton = findViewById(R.id.battleButton);
        logoutButton = findViewById(R.id.logoutButton);

        currentUser = mAuth.getCurrentUser();
        battleButton = findViewById(R.id.battleButton);

        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users/" + currentUser.getUid());
    }

    public void onStart() {
        super.onStart();

        if (currentUser != null) {

         userRef.child("Username").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 hsText.setText("Welcome, "+ dataSnapshot.getValue().toString());
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
        }

        listViewButton.setOnClickListener(e -> {
            startActivity(new Intent(HomeScreenView.this, PokemonList.class));

        });

        battleButton.setOnClickListener(e -> {
            startActivity(new Intent(HomeScreenView.this, PokemonSelectBattleList.class));

        });

        logoutButton.setOnClickListener(e ->{
            mAuth.signOut();
            startActivity(new Intent(HomeScreenView.this, LoginView.class));
        });
    }
}
