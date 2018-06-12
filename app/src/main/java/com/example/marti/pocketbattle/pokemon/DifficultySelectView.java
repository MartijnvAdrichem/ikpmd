package com.example.marti.pocketbattle.pokemon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;

import java.util.concurrent.ThreadLocalRandom;

public class DifficultySelectView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_select_view);
    }


    public void selectEasy(View v){
        goToNext(1);
    }
    public void selectMedium(View v){
        goToNext(2);

    }
    public void selectHard(View v){
        goToNext(3);

    }
    public void selectElite(View v){
        goToNext(4);

    }

    public void goToNext(int diff){
        Intent intent = new Intent(DifficultySelectView.this, PokemonSelectBattleList.class);
        Bundle b = new Bundle();
        b.putInt("difficulty", diff);
        intent.putExtras(b);
        startActivity(intent);
    }
}
