package com.example.marti.pocketbattle.pokemon;

import android.util.Log;

import com.example.marti.pocketbattle.models.Pokemon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PokemonFirebase {

    private static PokemonFirebase pokemonFirebase;
    private DatabaseReference mDatabase;

    public static PokemonFirebase getPokemonFirebase(){
        if(pokemonFirebase == null){
           pokemonFirebase = new PokemonFirebase();

        }
        return pokemonFirebase;
    }

    public PokemonFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public ArrayList<Pokemon> getAllPokemon(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pokemon");
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        myRef.limitToFirst(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Pokemon pokemon = d.getValue(Pokemon.class);
                    pokemons.add(pokemon);
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return pokemons;
    }

}
