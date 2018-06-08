package com.example.marti.pocketbattle.shop;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marti.pocketbattle.HomeScreenView;
import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;

import java.io.IOException;
import java.io.InputStream;

public class BuyEggView extends AppCompatActivity {

    Pokemon pokemon;
    ImageView imageView;
    TextView textView;

    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_egg_view);
        imageView = findViewById(R.id.image_egg);
        textView = findViewById(R.id.textview_pokemon_name);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            pokemon = (Pokemon) b.getSerializable("pokemon");
        }

        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                BuyEggView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream ims = getAssets().open("pokemon/" + pokemon.id + ".png");
                            Drawable d = Drawable.createFromStream(ims, null);

                            imageView.setImageDrawable(d);
                            textView.setText("its a " + pokemon.identifier + "!");


                        } catch(IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(BuyEggView.this, ShopView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        };

        thread.start();

    }


}

