package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marti.pocketbattle.LoginView;
import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.RegisterView;
import com.example.marti.pocketbattle.models.Move;
import com.example.marti.pocketbattle.models.Pokemon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PokemonBattleArea extends AppCompatActivity {


    private List<Pokemon> userSelectedPokemon;
    private List<Pokemon> computerSelectedPokemon;

    Pokemon enemyFightingPokemon;
    int enemyFightingPos = 0;
    Pokemon userFightingPokemon;
    int userFightingPos = 0;

    private ProgressBar enemyHpbar;
    private TextView enemyName;
    private TextView enemyLevel;
    private TextView enemyHitpoints;
    private ImageView enemyImageView;

    private ProgressBar pokemonHpbar;
    private TextView pokemonName;
    private TextView pokemonLevel;
    private TextView pokemonHitpoints;
    private ImageView pokemonImageView;

    private ListView moveList;
    private MoveAdapter moveAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_battle_area);
        userSelectedPokemon = new ArrayList<>();

        enemyHitpoints = findViewById(R.id.enemy_hp);
        enemyHpbar = findViewById(R.id.enemy_hpbar);
        enemyName = findViewById(R.id.enemy_name);
        enemyImageView = findViewById(R.id.enemy_image);
        enemyLevel = findViewById(R.id.enemy_level);

        pokemonHitpoints = findViewById(R.id.pokemon_hp);
        pokemonHpbar = findViewById(R.id.pokemon_hpbar);
        pokemonName = findViewById(R.id.pokemon_name);
        pokemonImageView = findViewById(R.id.pokemon_image);
        pokemonLevel = findViewById(R.id.pokemon_level);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            userSelectedPokemon.add((Pokemon) b.getSerializable("1"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("2"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("3"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("4"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("5"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("6"));
        }
        getRandomPokemonAI(0);

        moveList = (ListView) findViewById(R.id.move_list);
        moveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                 // ArrayList<Pokemon> pokemons = new ArrayList<>();
                                                 Move move = userFightingPokemon.moves.get(position);
                                                 if(move.ppLeft > 0){
                                                     move.ppLeft -= 1;
                                                     userAttack(move);
                                                     updateUI();
                                                 }
                                             }
                                         }
        );



    }
    private void userAttack(Move move){
        if(userFightingPokemon.level > enemyFightingPokemon.level) {
          if(!calculateMoveDamage(userFightingPokemon, enemyFightingPokemon, move)) {
             if(calculateMoveDamage(enemyFightingPokemon, userFightingPokemon, enemyFightingPokemon.moves.get(ThreadLocalRandom.current().nextInt(0, enemyFightingPokemon.moves.size())))){
                 killUserPokemon();
             }
          } else {
              killEnemyPokemon();
          }
        } else {
            if(calculateMoveDamage(enemyFightingPokemon, userFightingPokemon, enemyFightingPokemon.moves.get(ThreadLocalRandom.current().nextInt(0, enemyFightingPokemon.moves.size())))) {
                if( !calculateMoveDamage(userFightingPokemon, enemyFightingPokemon, move)){
                    killEnemyPokemon();
                }
            } else {
                killUserPokemon();
            }

        }
    }


    private boolean calculateMoveDamage(Pokemon attackingPokemon, Pokemon defendingPokemon, Move move){

        int damage = ((((((2 * attackingPokemon.level) / 5) + 2) * move.power * (attackingPokemon.attack / defendingPokemon.defence)) / 50) +2);
        defendingPokemon.currentHp -= damage;
        return defendingPokemon.currentHp < 0;
    }

    private void killEnemyPokemon(){
        enemyFightingPos++;
        if(enemyFightingPos == 6){
            return;
        }
        enemyFightingPokemon = computerSelectedPokemon.get(enemyFightingPos);
        updateUI();
    }

    private void killUserPokemon(){
        userFightingPos++;
        if(userFightingPos == 6){
            return;
        }
        userFightingPokemon = userSelectedPokemon.get(userFightingPos);
        moveAdapter = new MoveAdapter(PokemonBattleArea.this,  0, userFightingPokemon.moves);
        moveList.setAdapter(moveAdapter);
        updateUI();
    }
    private void getRandomPokemonAI(int difficulty){
        int userPokemonsLevel  = 0;
        for (Pokemon pokemon: userSelectedPokemon) {
            userPokemonsLevel += pokemon.level;
        }

        userPokemonsLevel /= 6;
        final int averageUserPokemonLevel = userPokemonsLevel;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference pokemonRef = database.getReference("pokemon");
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        computerSelectedPokemon = new ArrayList<>();

        pokemonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Pokemon pokemon = d.getValue(Pokemon.class);
                    pokemon.key = d.getKey();
                    pokemons.add(pokemon);
                }

                for (int i = 0; i < 6; i++) {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 151);
                    Pokemon pokemon = new Pokemon(pokemons.get(randomNum));
                    pokemon.levelUpMultiple(averageUserPokemonLevel);// + ThreadLocalRandom.current().nextInt(0 , 5));
                    computerSelectedPokemon.add(pokemon);
                }
                initializeBattle();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void updateUI(){
        moveAdapter.notifyDataSetChanged();

        enemyHitpoints.setText(enemyFightingPokemon.hp + "");
        enemyHpbar.setMax(enemyFightingPokemon.hp);
        enemyHpbar.setProgress(enemyFightingPokemon.currentHp);
        enemyName.setText(enemyFightingPokemon.identifier);
        enemyLevel.setText(enemyFightingPokemon.level + "");

        try {
            InputStream ims = getAssets().open("pokemon/" + enemyFightingPokemon.id + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            enemyImageView.setImageDrawable(d);
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        pokemonHitpoints.setText(userFightingPokemon.hp + "");
        pokemonHpbar.setMax(userFightingPokemon.hp);
        pokemonHpbar.setProgress(userFightingPokemon.currentHp);
        pokemonName.setText(userFightingPokemon.identifier);
        pokemonLevel.setText(userFightingPokemon.level + "");

        try {
            InputStream ims = getAssets().open("pokemon/back/" + userFightingPokemon.id + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            pokemonImageView.setImageDrawable(d);
        } catch(IOException ex) {
            ex.printStackTrace();
        }


    }
    public void initializeBattle(){

        enemyFightingPokemon = computerSelectedPokemon.get(0);
        userFightingPokemon = userSelectedPokemon.get(0);

        for (Pokemon pokemon : userSelectedPokemon) {
            pokemon.currentHp = pokemon.hp;
            for (Move move : pokemon.moves){
                move.ppLeft = move.pp;
            }
        }

        for (Pokemon pokemon : computerSelectedPokemon) {
            pokemon.currentHp = pokemon.hp;
            for (Move move : pokemon.moves){
                move.ppLeft = move.pp;
            }
        }
        moveAdapter = new MoveAdapter(PokemonBattleArea.this,  0, userFightingPokemon.moves);
        moveList.setAdapter(moveAdapter);
        updateUI();


    }
}
