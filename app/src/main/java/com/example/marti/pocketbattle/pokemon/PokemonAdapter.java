package com.example.marti.pocketbattle.pokemon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PokemonAdapter  extends ArrayAdapter<Pokemon> {


    public PokemonAdapter(Context context, int resource, List<Pokemon> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null ) {
            vh = new ViewHolder();
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.view_pokemonlist_row, parent, false);
            vh.name = (TextView) convertView.findViewById(R.id.pokemon_name);
            vh.pokemonImage = (ImageView) convertView.findViewById(R.id.pokemon_image);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Pokemon pokemon = getItem(position);

        try {
            InputStream ims = getContext().getAssets().open("pokemon/" + pokemon.id + ".png");
            Drawable d = Drawable.createFromStream(ims, null);
            vh.pokemonImage.setImageDrawable(d);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        vh.name.setText((CharSequence) pokemon.name);
//        int resID = getContext().getResources().getIdentifier("pokemon/0.png", "drawable",  getContext().getPackageName());
//        vh.pokemonImage.setImageResource(resID);
//        Picasso.get().load("file:///assets/1.png").resize(160, 160).into(vh.pokemonImage);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        ImageView pokemonImage;
    }

}
