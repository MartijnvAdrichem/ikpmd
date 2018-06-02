package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.marti.pocketbattle.pokemon.PokemonList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HomeScreenView extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView hsText;
    public static FirebaseUser currentUser;
    Button listViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_view);
        mAuth = FirebaseAuth.getInstance();
        hsText = (TextView) findViewById(R.id.hsText);
        listViewButton = findViewById(R.id.listviewbutton);
        currentUser = mAuth.getCurrentUser();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
        if (currentUser != null) {
            hsText.setText(currentUser.getEmail().toString());
        }

        listViewButton.setOnClickListener(e -> {
            startActivity(new Intent(HomeScreenView.this, PokemonList.class));

        });

    }
}
