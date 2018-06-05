package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marti.pocketbattle.HomeScreenView;
import com.example.marti.pocketbattle.LoginView;
import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PokemonSelectBattleList extends AppCompatActivity {


    private ListView listView;
    private Button goBattleButton;
    private PokemonAdapter mAdapter;
    private List<Pokemon> pokemons = new ArrayList<>();
    private List<Pokemon> selectedPokemon = new ArrayList<>();

    private static PokemonFirebase pokemonFirebase;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_select_battle_list);

        listView = (ListView) findViewById(R.id.my_list_view);
        goBattleButton = findViewById(R.id.start_button);
        selectedPokemon = new ArrayList<>();

        goBattleButton.setOnClickListener(e -> {
            if(this.selectedPokemon.size() < 6){
                Toast.makeText(PokemonSelectBattleList.this, "You need to select 6 pokemon", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(PokemonSelectBattleList.this, PokemonBattleArea.class);
                Bundle b = new Bundle();
                int i = 0;
                for (Pokemon pokemon:selectedPokemon) {
                    i++;
                    b.putSerializable(i + "", pokemon);
                }
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                 if(selectedPokemon.contains(pokemons.get(position))){
                                                     selectedPokemon.remove(pokemons.get(position));
                                                     view.setBackgroundColor(0);

                                                 } else {
                                                        view.setBackgroundColor(Color.RED);
                                                     selectedPokemon.add(pokemons.get(position));
                                                 }
                                             }
                                         }
        );
        pokemons = getAllPokemon();


    }

    public ArrayList<Pokemon> getAllPokemon(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + HomeScreenView.currentUser.getUid() + "/pokemon");
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Pokemon pokemon = d.getValue(Pokemon.class);
                    pokemon.key = d.getKey();
                    pokemons.add(pokemon);
                }
                mAdapter = new PokemonAdapter(PokemonSelectBattleList.this, 0, pokemons);
                listView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return pokemons;
    }

}
