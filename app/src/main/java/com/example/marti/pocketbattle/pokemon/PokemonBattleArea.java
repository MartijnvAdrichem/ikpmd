package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marti.pocketbattle.HomeScreenView;
import com.example.marti.pocketbattle.LoginView;
import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.RegisterView;
import com.example.marti.pocketbattle.models.Move;
import com.example.marti.pocketbattle.models.Pokemon;
import com.example.marti.pocketbattle.models.User;
import com.example.marti.pocketbattle.shop.BuyEggView;
import com.example.marti.pocketbattle.shop.ShopView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PokemonBattleArea extends AppCompatActivity {


    private List<Pokemon> userSelectedPokemon;
    private List<Pokemon> computerSelectedPokemon;
    private ArrayList<Integer> addExperiencesToPokemon = new ArrayList<>();

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

    private TextView textviewMssages;

    private DatabaseReference userRef;
    FirebaseDatabase database;

    int difficulty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
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
        textviewMssages = findViewById(R.id.textview_messages);
        pokemonHpbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        enemyHpbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        addExperiencesToPokemon.add(0);
        addExperiencesToPokemon.add(0);
        addExperiencesToPokemon.add(0);
        addExperiencesToPokemon.add(0);
        addExperiencesToPokemon.add(0);
        addExperiencesToPokemon.add(0);



        Bundle b = getIntent().getExtras();
        if (b != null) {
            userSelectedPokemon.add((Pokemon) b.getSerializable("1"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("2"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("3"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("4"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("5"));
            userSelectedPokemon.add((Pokemon) b.getSerializable("6"));
            difficulty = b.getInt("difficulty");

        }

        getRandomPokemonAI(difficulty);

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
          calculateMoveDamage(false, true, userFightingPokemon, enemyFightingPokemon, move, move);
        } else {
            calculateMoveDamage(true, true, enemyFightingPokemon, userFightingPokemon, enemyFightingPokemon.moves.get(ThreadLocalRandom.current().nextInt(0, enemyFightingPokemon.moves.size())), move);
                }
    }


    private void calculateMoveDamage(boolean isEnemy, boolean attackBack,  Pokemon attackingPokemon, Pokemon defendingPokemon, Move move, Move userMove){
        moveList.setEnabled(false);
        moveList.setVisibility(View.INVISIBLE);
        int accuracy = move.accuracy;
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 101);
         int damage = 0;
        if(randomNumber < accuracy) {
            double part1 = 2.0 * attackingPokemon.level;
            double part2 = part1 / 5.0;
            double part3 = part2 + 2;
            double part4 = part3 * move.power;
            double part5 = (attackingPokemon.attack + 0.0) / (defendingPokemon.defence + 0.0);
            double part6 = part4 * part5;
            double part7 = part6 / 50;
            double part8 = part7 + 2;
            int part9 = (int) part7;
            if (part9 > defendingPokemon.currentHp) {
                damage = defendingPokemon.currentHp;
             } else{
                damage = part9;
            }

        } else {
            damage = 0;
        }
        final int finaldamage = damage;

        textviewMssages.setText(attackingPokemon.identifier + " used " + move.identifier + (finaldamage > 0 ?(" and did " + finaldamage + " damage.") : ", but missed."));
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.pokemonattack);
        final Animation enemyAttack = AnimationUtils.loadAnimation(this, R.anim.enemypokemonattack);
        final Animation enemydefence = AnimationUtils.loadAnimation(this, R.anim.enemypokemondefence);
        final Animation animdefence = AnimationUtils.loadAnimation(this, R.anim.pokemondefence);

        if(isEnemy){
            HomeScreenView.user.damageTaken += finaldamage;
            pokemonImageView.startAnimation(enemydefence);
            enemyImageView.startAnimation(animScale);
            pokemonImageView.setColorFilter(Color.argb(120, 242, 0, 0));
            ProgressBarAnimation anim = new ProgressBarAnimation(pokemonHpbar, defendingPokemon.currentHp, defendingPokemon.currentHp - damage);
                anim.setDuration(1000);
                pokemonHpbar.startAnimation(anim);
            } else {
            HomeScreenView.user.damageDone += finaldamage;
            enemyImageView.startAnimation(animdefence);
            pokemonImageView.startAnimation(enemyAttack);
            enemyImageView.setColorFilter(Color.argb(120, 242, 0, 0));

            ProgressBarAnimation anim = new ProgressBarAnimation(enemyHpbar, defendingPokemon.currentHp, defendingPokemon.currentHp - damage);
                anim.setDuration(1000);
                enemyHpbar.startAnimation(anim);
            }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pokemonImageView.setColorFilter(Color.argb(0, 0, 0, 0));
                enemyImageView.setColorFilter(Color.argb(0, 0, 0, 0));

                defendingPokemon.currentHp -= finaldamage;
                if(defendingPokemon.currentHp <= 0) {
                    moveList.setEnabled(true);
                    moveList.setVisibility(View.VISIBLE);
                    if(isEnemy) {
                        killUserPokemon();
                    } else {
                        killEnemyPokemon();
                    }
                } else if(attackBack) {
                    if(isEnemy){
                        calculateMoveDamage(false, false, userFightingPokemon, enemyFightingPokemon, userMove, userMove);
                    } else {
                        calculateMoveDamage(true, false, enemyFightingPokemon, userFightingPokemon, enemyFightingPokemon.moves.get(ThreadLocalRandom.current().nextInt(0, enemyFightingPokemon.moves.size())), userMove);

                    }
                }
                updateUI();
                if(!attackBack) {
                    moveList.setEnabled(true);
                    moveList.setVisibility(View.VISIBLE);
                }
            }
        }, 2500);




    }
    @Override
    public void onBackPressed() {

    }

    private void killEnemyPokemon(){
        textviewMssages.setText(enemyFightingPokemon.identifier + " fainted.");
        enemyFightingPos++;
        if(enemyFightingPos >= 6){
            winGame();
            return;
        }

        int xp = 50 + enemyFightingPokemon.hp / 5 + (enemyFightingPokemon.level - userFightingPokemon.level) * 2 ;
        if(xp < 0){
            xp = 0;
        }

        addExperiencesToPokemon.set(userFightingPos, addExperiencesToPokemon.get(userFightingPos) + xp);


        enemyFightingPokemon = computerSelectedPokemon.get(enemyFightingPos);
        enemyHpbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        enemyHpbar.setProgress(userFightingPokemon.currentHp);
        enemyHpbar.setMax(userFightingPokemon.hp);
        updateUI();
    }

    private void killUserPokemon(){
        textviewMssages.setText(userFightingPokemon.identifier + " fainted.");
        userFightingPos++;
        if(userFightingPos >= 6){
            loseGame();
            return;
        }
        userFightingPokemon = userSelectedPokemon.get(userFightingPos);
        moveAdapter = new MoveAdapter(PokemonBattleArea.this,  0, userFightingPokemon.moves);
        moveList.setAdapter(moveAdapter);
        pokemonHpbar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        pokemonHpbar.setProgress(userFightingPokemon.currentHp);
        pokemonHpbar.setMax(userFightingPokemon.hp);
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
                    int randomNum = ThreadLocalRandom.current().nextInt(0, pokemons.size() -1 );
                    Pokemon pokemon = new Pokemon(pokemons.get(randomNum));
                    pokemon.levelUpMultiple((int)(averageUserPokemonLevel * (Math.random() + 0.2 * difficulty)));// + ThreadLocalRandom.current().nextInt(0 , 5));
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

        enemyHitpoints.setText(enemyFightingPokemon.currentHp + "");
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

        pokemonHitpoints.setText(userFightingPokemon.currentHp + "");
        pokemonName.setText(userFightingPokemon.identifier);
        pokemonHpbar.setMax(userFightingPokemon.hp);
        pokemonHpbar.setProgress(userFightingPokemon.currentHp);
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
            pokemon.moves.removeAll(Collections.singleton(null));
            pokemon.currentHp = pokemon.hp;
            if(pokemon.moves == null) {
                continue;
            }
            for (Move move : pokemon.moves){
                if(move == null){
                    continue;
                }
                move.ppLeft = move.pp;
            }
        }

        for (Pokemon pokemon : computerSelectedPokemon) {
            pokemon.moves.removeAll(Collections.singleton(null));
            pokemon.moves.forEach((move) -> {
                        if (move == null) {
                            pokemon.moves.remove(move);
                        }
                    });
            pokemon.currentHp = pokemon.hp;
            if(pokemon.moves == null) {
                continue;
            }
            for (Move move : pokemon.moves){
                if(move == null){
                    continue;
                }
                move.ppLeft = move.pp;
            }
        }
        moveAdapter = new MoveAdapter(PokemonBattleArea.this,  0, userFightingPokemon.moves);
        moveList.setAdapter(moveAdapter);
        updateUI();


    }

    public void winGame(){

        HomeScreenView.user.addWin();

        Intent intent = new Intent(PokemonBattleArea.this, EndBattleView.class);
        Bundle b = new Bundle();
        int i = 0;
        for (Pokemon pokemon:userSelectedPokemon) {
            i++;
            b.putSerializable(i + "", pokemon);
        }

        for (int j = 0; j < addExperiencesToPokemon.size(); j++) {
            addExperiencesToPokemon.set(j, addExperiencesToPokemon.get(j) + 50 * difficulty);
        }
        b.putInt("coins", ThreadLocalRandom.current().nextInt(10 * difficulty, 50 + (20 * difficulty)));
        b.putInt("xp", ThreadLocalRandom.current().nextInt(75 + 10 * difficulty, 125 + 25 * difficulty));
        b.putBoolean("win", true);
        b.putIntegerArrayList("xpGains", addExperiencesToPokemon);
        intent.putExtras(b);
        startActivity(intent);

        //HomeScreenView.user.addXP(ThreadLocalRandom.current().nextInt(100, 150));
        //HomeScreenView.updateUser();
    }

    public void loseGame(){

        HomeScreenView.user.addLose();

        Intent intent = new Intent(PokemonBattleArea.this, EndBattleView.class);
        Bundle b = new Bundle();
        int i = 0;
        for (Pokemon pokemon:userSelectedPokemon) {
            i++;
            b.putSerializable(i + "", pokemon);
        }
        b.putInt("coins", ThreadLocalRandom.current().nextInt(5 * difficulty, 15 + (5 * difficulty)));
        b.putInt("xp", ThreadLocalRandom.current().nextInt(20 + 5 * difficulty, 30 + 5 * difficulty));
        b.putBoolean("win", false);
        b.putIntegerArrayList("xpGains", addExperiencesToPokemon);
        intent.putExtras(b);
        startActivity(intent);

    }
}
