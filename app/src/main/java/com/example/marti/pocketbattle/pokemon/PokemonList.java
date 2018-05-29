package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PokemonList extends AppCompatActivity {

    private ListView mListView;
    private PokemonAdapter mAdapter;
    private List<Pokemon> pokemons = new ArrayList<>();

    private static PokemonFirebase pokemonFirebase;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);

        mListView = (ListView) findViewById(R.id.my_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                 Intent intent = new Intent(PokemonList.this, PokemonDetailView.class);
                                                 Bundle b = new Bundle();
                                                 b.putSerializable("pokemon", pokemons.get(position));
                                                 intent.putExtras(b);
                                                 startActivity(intent);
                                             }
                                         }
        );
        pokemons = getAllPokemon();

    }

    public ArrayList<Pokemon> getAllPokemon(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Pokemon");
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        myRef.limitToFirst(152).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Pokemon pokemon = d.getValue(Pokemon.class);
                    pokemons.add(pokemon);
                }
                mAdapter = new PokemonAdapter(PokemonList.this, 0, pokemons);
                mListView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return pokemons;
    }
}
