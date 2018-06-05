package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marti.pocketbattle.models.Pokemon;
import com.example.marti.pocketbattle.pokemon.PokemonAdapter;
import com.example.marti.pocketbattle.pokemon.PokemonList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        username = (EditText) findViewById(R.id.usernameRegField) ;
        password = (EditText) findViewById(R.id.passwordRegField);
        email = (EditText) findViewById(R.id.emailRegField);
        regButton = (Button) findViewById(R.id.registerRegButton);

        mAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener( event -> {
            createAccount(email.getText().toString(), password.getText().toString());
        });
    }

    public void createAccount(String username, String password){
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnSuccessListener(RegisterView.this, new OnSuccessListener<AuthResult>(){
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        giveUserPokemon();
                    }
                });
    }


    public void giveUserPokemon(){
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users/" + 2 + "/");
        DatabaseReference pokemonRef = database.getReference("pokemon");

      //  myRef.push().setValue("username", username);

            ArrayList<Pokemon> pokemons = new ArrayList<>();

        pokemonRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Pokemon pokemon = d.getValue(Pokemon.class);
                        pokemon.key = d.getKey();
                        pokemons.add(pokemon);
                    }

                    for (int i = 0; i < 6; i++) {
                        int randomNum = ThreadLocalRandom.current().nextInt(0, 50);
                        Pokemon pokemon = new Pokemon(pokemons.get(randomNum));
                        pokemon.xpForNextLevel = pokemon.base_experience;
                        pokemon.levelUpMultiple(250);
                        //pokemon.addExperience(ThreadLocalRandom.current().nextInt(0, 50000));
                        pokemon.levelup(true);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("users/" + user.getUid() + "/pokemon/");
                        myRef.push().setValue(pokemon);
                    }
                    startActivity(new Intent(RegisterView.this, LoginView.class));
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }
}
