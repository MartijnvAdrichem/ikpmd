package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.marti.pocketbattle.models.Pokemon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterView extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private static final String TAG = "LoginView";

    EditText username;
    EditText password;
    EditText email;
    Button regButton;
    RadioButton bulbasaurButton;
    RadioButton charmanderButton;
    RadioButton squirtleButton;
    RadioGroup radioGroup;

    RadioButton radioButton;

    FirebaseDatabase database;
    FirebaseUser user;

    DatabaseReference userRef;
    DatabaseReference pokemonRef;
    DatabaseReference allePokemonRef;

    int starterPokemonNr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        username = (EditText) findViewById(R.id.usernameRegField) ;
        password = (EditText) findViewById(R.id.passwordRegField);
        email = (EditText) findViewById(R.id.emailRegField);
        regButton = (Button) findViewById(R.id.registerRegButton);

        bulbasaurButton = findViewById(R.id.bulbasaurButton);
        charmanderButton = findViewById(R.id.charmanderButton);
        squirtleButton = findViewById(R.id.squirtleButton);

        radioGroup = findViewById(R.id.radioGroup);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        allePokemonRef = database.getReference("pokemon");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(bulbasaurButton.isChecked()) {
                    starterPokemonNr = 0;
                } else if(charmanderButton.isChecked()) {
                    starterPokemonNr = 3;
                } else if(squirtleButton.isChecked()) {
                    starterPokemonNr = 6;
                } else {
                    starterPokemonNr = ThreadLocalRandom.current().nextInt(0,6);
                }
            }
        });

        regButton.setOnClickListener( event -> {
            createAccount(email.getText().toString(), password.getText().toString());
        });
    }

    public void createAccount(String username, String password){
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnSuccessListener(RegisterView.this, new OnSuccessListener<AuthResult>(){
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        singIn(username, password);

                    }
                });


    }

    public void singIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            getReferences();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegisterView.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void getReferences(){
        user = mAuth.getCurrentUser();
        userRef = database.getReference("users/" + user.getUid() + "/user");
        pokemonRef = database.getReference("users/" + user.getUid() + "/pokemon/");

        userRef.child("username").setValue(username.getText().toString());
        userRef.child("level").setValue(1);
        userRef.child("currentXp").setValue(0);
        userRef.child("nextLevelXp").setValue(100);
        userRef.child("coins").setValue(25);

        giveUserPokemon();
    }



    public void giveUserPokemon(){
            ArrayList<Pokemon> pokemons = new ArrayList<>();

        allePokemonRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Pokemon pokemon = d.getValue(Pokemon.class);
                        pokemon.key = d.getKey();
                        if(pokemon != null) {
                            pokemons.add(pokemon);
                        }
                    }

                    Pokemon starterPokemon = new Pokemon(pokemons.get(starterPokemonNr));

                    System.out.println("STARTER POKEMON IS: " + starterPokemonNr);
                    addPokemonToDatabase(starterPokemon);

                    for (int i = 0; i < 5; i++) {
                        int randomNum = ThreadLocalRandom.current().nextInt(9, 50);
                        Pokemon pokemon = new Pokemon(pokemons.get(randomNum));
                        addPokemonToDatabase(pokemon);

                    }
                    startActivity(new Intent(RegisterView.this, LoginView.class));
                }

                public void addPokemonToDatabase(Pokemon pokemon){
                    pokemon.xpForNextLevel = pokemon.base_experience;
                    //pokemon.addExperience(ThreadLocalRandom.current().nextInt(0, 50000));
                    pokemon.levelup(true);
                    pokemonRef.push().setValue(pokemon);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
}
