package com.example.marti.pocketbattle.pokemon;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;

import java.io.IOException;
import java.io.InputStream;

public class PokemonDetailView extends AppCompatActivity {

    Pokemon pokemon;

    TextView nameTextView;
    TextView levelTextView;
    TextView currentXpTextView;
    TextView nextLevelXpTextView;
    ProgressBar levelProgessBar;
    ImageView pokemonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail_view);
        nameTextView = findViewById(R.id.pokemon_name_label);
        levelTextView = findViewById(R.id.pokemon_level_label);
        currentXpTextView = findViewById(R.id.pokeon_currentxp_label);
        nextLevelXpTextView = findViewById(R.id.pokemon_nextlevelxp_label);
        levelProgessBar = findViewById(R.id.pokemon_levelprogress_bar);
        pokemonImage = findViewById(R.id.pokemon_image);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            pokemon = (Pokemon)b.getSerializable("pokemon");
        }

        if(pokemon != null){
            nameTextView.setText(pokemon.name);
            levelTextView.setText(pokemon.level + "");
            currentXpTextView.setText(pokemon.experience + "");
            nextLevelXpTextView.setText(pokemon.xpForNextLevel + "");
            levelProgessBar.setMax(pokemon.xpForNextLevel - pokemon.beginXpLevel);
            levelProgessBar.setProgress(pokemon.experience - pokemon.beginXpLevel);

            try {
                InputStream ims = getAssets().open("pokemon/" + pokemon.id + ".png");
                Drawable d = Drawable.createFromStream(ims, null);
                pokemonImage.setImageDrawable(d);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        }



}
