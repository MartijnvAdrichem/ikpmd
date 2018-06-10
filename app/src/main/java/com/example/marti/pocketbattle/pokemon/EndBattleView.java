package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marti.pocketbattle.HomeScreenView;
import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;
import com.example.marti.pocketbattle.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class EndBattleView extends AppCompatActivity {

    ArrayList<Pokemon> pokemons = new ArrayList<>();
    ArrayList<Integer> addExperienceToPokemon;
    boolean isWin = false;
    int xp;
    int coins;
    User user;

    TextView textViewCoins;
    TextView textViewXp;
    TextView userCurrentLv;
    TextView userNextLv;
    ProgressBar userXpProgress;
    TextView textViewIsWin;
    TextView textViewIsLevelUp;

    ImageView pokemonImage1;
    TextView pokemonCurrentLevel1;
    TextView pokemonNextLevel1;


    ImageView pokemonImage2;
    TextView pokemonCurrentLevel2;
    TextView pokemonNextLevel2;


    ImageView pokemonImage3;
    TextView pokemonCurrentLevel3;
    TextView pokemonNextLevel3;


    ImageView pokemonImage4;
    TextView pokemonCurrentLevel4;
    TextView pokemonNextLevel4;


    ImageView pokemonImage5;
    TextView pokemonCurrentLevel5;
    TextView pokemonNextLevel5;


    ImageView pokemonImage6;
    TextView pokemonCurrentLevel6;
    TextView pokemonNextLevel6;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_battle_view);
        database = FirebaseDatabase.getInstance();


         textViewCoins = findViewById(R.id.textview_coins);
         textViewXp = findViewById(R.id.textView_xp);
         userCurrentLv = findViewById(R.id.textview_current_level);
         userNextLv = findViewById(R.id.textview_next_level);
         userXpProgress = findViewById(R.id.userXpProgess);
         textViewIsWin = findViewById(R.id.textview_wonlose);
        textViewIsLevelUp = findViewById(R.id.textview_levelup);
        textViewIsLevelUp.setVisibility(View.INVISIBLE);

        pokemonImage1 = findViewById(R.id.levelup_pokemon_image);
        pokemonImage2 = findViewById(R.id.levelup_pokemon_image2);
        pokemonImage3 = findViewById(R.id.levelup_pokemon_image3);
        pokemonImage4 = findViewById(R.id.levelup_pokemon_image4);
        pokemonImage6 = findViewById(R.id.levelup_pokemon_image5);
        pokemonImage5 = findViewById(R.id.levelup_pokemon_image6);

        pokemonCurrentLevel1 = findViewById(R.id.levelup_pokemon_oldlevel);
        pokemonCurrentLevel2 = findViewById(R.id.levelup_pokemon_oldlevel4);
        pokemonCurrentLevel3 = findViewById(R.id.levelup_pokemon_oldlevel5);
        pokemonCurrentLevel4 = findViewById(R.id.levelup_pokemon_oldlevel6);
        pokemonCurrentLevel5 = findViewById(R.id.levelup_pokemon_oldlevel7);
        pokemonCurrentLevel6 = findViewById(R.id.levelup_pokemon_oldlevel8);

        pokemonNextLevel1 = findViewById(R.id.levelup_pokemon_newLevel);
        pokemonNextLevel2 = findViewById(R.id.levelup_pokemon_newLevel4);
        pokemonNextLevel3 = findViewById(R.id.levelup_pokemon_newLevel5);
        pokemonNextLevel4 = findViewById(R.id.levelup_pokemon_newLevel6);
        pokemonNextLevel5 = findViewById(R.id.levelup_pokemon_newLevel7);
        pokemonNextLevel6 = findViewById(R.id.levelup_pokemon_newLevel8);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            pokemons.add((Pokemon) b.getSerializable("1"));
            pokemons.add((Pokemon) b.getSerializable("2"));
            pokemons.add((Pokemon) b.getSerializable("3"));
            pokemons.add((Pokemon) b.getSerializable("4"));
            pokemons.add((Pokemon) b.getSerializable("5"));
            pokemons.add((Pokemon) b.getSerializable("6"));
        }

        setPokemonImage(pokemons.get(0), pokemonImage1);
        setPokemonImage(pokemons.get(1), pokemonImage2);
        setPokemonImage(pokemons.get(2), pokemonImage3);
        setPokemonImage(pokemons.get(3), pokemonImage4);
        setPokemonImage(pokemons.get(4), pokemonImage5);
        setPokemonImage(pokemons.get(5), pokemonImage6);

        pokemonCurrentLevel1.setText(pokemons.get(0).level + "");
        pokemonCurrentLevel2.setText(pokemons.get(1).level + "");
        pokemonCurrentLevel3.setText(pokemons.get(2).level + "");
        pokemonCurrentLevel4.setText(pokemons.get(3).level + "");
        pokemonCurrentLevel5.setText(pokemons.get(4).level + "");
        pokemonCurrentLevel6.setText(pokemons.get(5).level + "");



        isWin = b.getBoolean("win");
        xp = b.getInt("xp");
        coins = b.getInt("coins");
        addExperienceToPokemon = b.getIntegerArrayList("xpGains");
        user = HomeScreenView.user;

        for (int i = 0; i < 6 ; i++) {
            pokemons.get(i).addExperience(addExperienceToPokemon.get(i));
        }
        pokemonNextLevel1.setText(pokemons.get(0).level + "");
        pokemonNextLevel2.setText(pokemons.get(1).level + "");
        pokemonNextLevel3.setText(pokemons.get(2).level + "");
        pokemonNextLevel4.setText(pokemons.get(3).level + "");
        pokemonNextLevel5.setText(pokemons.get(4).level + "");
        pokemonNextLevel6.setText(pokemons.get(5).level + "");

        textViewCoins.setText(coins + "");
        textViewIsWin.setText(isWin ? "You won!" : "You lost...");
        textViewXp.setText(xp + "");
        userCurrentLv.setText(user.level + "");
        userNextLv.setText((user.level + 1 ) + "");
        userXpProgress.setMax(user.nextLevelXp);
        userXpProgress.setProgress(user.currentXp);

        user.coins += coins;
        progressAnimation();
        HomeScreenView.updateUser();

        for (Pokemon pokemon :pokemons) {
            DatabaseReference pokemonRef = database.getReference("users/" + HomeScreenView.currentUser.getUid() + "/pokemon/" + pokemon.key);
            pokemonRef.removeValue();
            pokemonRef.setValue(pokemon);
        }
    }

    public void progressAnimation(){
        int xpLeft = 0;
        if(user.currentXp + xp > user.nextLevelXp){
            textViewIsLevelUp.setVisibility(View.VISIBLE);
            xpLeft = user.currentXp + xp - user.nextLevelXp;
            xp = user.nextLevelXp - user.currentXp;
        }
        final int XPLEFT = xpLeft;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                XpBarAnimation anim = new XpBarAnimation(userXpProgress, user.currentXp, user.currentXp + xp);
                anim.setDuration(1500);
                userXpProgress.startAnimation(anim);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        user.addXP(xp);
                        xp = XPLEFT;
                        userXpProgress.setMax(user.nextLevelXp);
                        userXpProgress.setProgress(user.currentXp);
                        userCurrentLv.setText(user.level + "");
                        userNextLv.setText((user.level + 1 ) + "");
                        if(xp > 0) {
                            progressAnimation();
                        }
                    }
                }, 1600);
            }
        }, 50);


    }

    @Override
    public void onBackPressed() {
        return;
    }


    public void goToHome(View v){
        Intent intent = new Intent(EndBattleView.this, HomeScreenView.class);
        startActivity(intent);
    }

    public void setPokemonImage(Pokemon pokemon, ImageView imageView){
        try {
            InputStream ims = getAssets().open("pokemon/" + pokemon.id + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
