package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonList extends AppCompatActivity {

    private ListView mListView;
    private PokemonAdapter mAdapter;
    private List<Pokemon> pokemons = new ArrayList<>();

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
        pokemons.add(new Pokemon(1, "Bulbasaur"));
        pokemons.add(new Pokemon(2, "Bulbasaur"));
        pokemons.add(new Pokemon(3, "Bulbasaur"));
        pokemons.add(new Pokemon(4, "Bulbasaur"));
        pokemons.add(new Pokemon(5, "Bulbasaur"));
        pokemons.add(new Pokemon(6, "Bulbasaur"));
        pokemons.add(new Pokemon(7, "Bulbasaur"));
        pokemons.add(new Pokemon(8, "Bulbasaur"));
        pokemons.add(new Pokemon(9, "Bulbasaur"));
        pokemons.add(new Pokemon(10, "Bulbasaur"));
        pokemons.add(new Pokemon(11, "Bulbasaur"));
        pokemons.add(new Pokemon(12, "Bulbasaur"));
        pokemons.add(new Pokemon(13, "Bulbasaur"));
        pokemons.add(new Pokemon(14, "Bulbasaur"));
        pokemons.add(new Pokemon(15, "Bulbasaur"));
        pokemons.add(new Pokemon(16, "Bulbasaur"));
        pokemons.add(new Pokemon(17, "Bulbasaur"));
        mAdapter = new PokemonAdapter(PokemonList.this, 0, pokemons);
        mListView.setAdapter(mAdapter);

    }
}
