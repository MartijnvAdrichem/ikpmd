package com.example.marti.pocketbattle.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marti.pocketbattle.HomeScreenView;
import com.example.marti.pocketbattle.LoginView;
import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.RegisterView;
import com.example.marti.pocketbattle.models.Pokemon;
import com.example.marti.pocketbattle.pokemon.PokemonBattleArea;
import com.example.marti.pocketbattle.pokemon.PokemonSelectBattleList;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ShopView extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference pokemonRef;
    DatabaseReference allePokemonRef;
    TextView coinsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_view);

        database = FirebaseDatabase.getInstance();
        pokemonRef = database.getReference("users/" + HomeScreenView.currentUser.getUid() + "/pokemon/");
        allePokemonRef = database.getReference("pokemon");

        coinsTextView = findViewById(R.id.textView_coins);

        coinsTextView.setText("Coins " + HomeScreenView.user.coins);

    }



    public void buySmallEgg(View v){
        int[] validPokemon = {0, 3, 6, 9, 11, 13, 16, 18, 20, 22, 26, 29, 32, 34, 36, 38, 43, 45, 47, 49, 51, 55, 57,
        62, 65, 70, 75, 77, 79, 82, 86, 88, 94, 96, 100, 105, 110, 112, 114, 116, 125, 134, 136 };
        giveUserPokemon(validPokemon, 150, 1);
    }

    public void buyMediumEgg(View v){
        int[] validPokemon = {1, 4, 7, 10, 12, 14, 17,19, 21, 23 , 24, 27, 30, 33, 35,37, 39, 40, 44, 46, 48, 50, 52, 56, 58, 60,
        63, 66, 68, 71, 73, 76, 78, 80, 83, 84, 87, 89, 92, 95, 97, 98, 101, 102, 104, 106, 113, 115, 117, 118, 119, 120,
        121, 122, 123, 129, 135, 137, 143, };
        giveUserPokemon(validPokemon, 400, 2);

    }

    public void buyLargeEgg(View v){
        int[] validPokemon = {2, 5, 8, 15, 25, 28, 31, 41, 42, 59, 61, 63, 64, 67,69, 72, 74, 81, 85, 90, 91,
        93, 99, 103, 107, 108, 111, 124, 126, 130, 131, 132, 133,138, 144, };
        giveUserPokemon(validPokemon, 1000, 3);

    }

    public void buyHugeEgg(View v){
        int[] validPokemon = {109, 127, 128, 139, 140, 141, 142, 145,  146, 147 };
        giveUserPokemon(validPokemon, 2500, 4);

    }


    public void giveUserPokemon(int[] validPokemon, int cost, int eggnr){
        ArrayList<Pokemon> pokemons = new ArrayList<>();

        if(HomeScreenView.user.coins < cost) {
            Toast.makeText(ShopView.this, "You need " + cost + " coins to buy this item", Toast.LENGTH_SHORT).show();
            return;

        }
            HomeScreenView.user.coins -= cost;
            HomeScreenView.updateUser();

        allePokemonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Pokemon pokemon = d.getValue(Pokemon.class);
                    pokemon.key = d.getKey();
                    if(pokemon != null) {
                        pokemons.add(pokemon);
                    }
                }


                    int randomNum = validPokemon[ThreadLocalRandom.current().nextInt(0, validPokemon.length)];
                    Pokemon pokemon = new Pokemon(pokemons.get(randomNum));
                    addPokemonToDatabase(pokemon);

                Intent intent = new Intent(ShopView.this, BuyEggView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b = new Bundle();
                b.putSerializable("pokemon", pokemon);
                b.putInt("eggimage",eggnr);
                intent.putExtras(b);
                startActivity(intent);
            }

            public void addPokemonToDatabase(Pokemon pokemon){
                pokemon.xpForNextLevel = pokemon.base_experience;
                pokemon.levelUpMultiple(ThreadLocalRandom.current().nextInt(HomeScreenView.user.level / 2, HomeScreenView.user.level));
                pokemonRef.push().setValue(pokemon);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}
