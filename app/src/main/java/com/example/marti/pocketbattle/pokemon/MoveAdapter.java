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
import com.example.marti.pocketbattle.models.Move;
import com.example.marti.pocketbattle.models.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MoveAdapter extends ArrayAdapter<Move> {


    public MoveAdapter(Context context, int resource, List<Move> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null ) {
            vh = new ViewHolder();
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.view_move_row, parent, false);
            vh.moveName = convertView.findViewById(R.id.move_name);
            vh.moveAccuracy =  convertView.findViewById(R.id.move_accuracy);
            vh.movePower =  convertView.findViewById(R.id.move_power);
            vh.moveMaxMoves =  convertView.findViewById(R.id.move_maxuse);
            vh.moveMovesLeft =  convertView.findViewById(R.id.move_useleft);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Move move = getItem(position);

        vh.moveName.setText((CharSequence) move.identifier);
        vh.moveAccuracy.setText(move.accuracy + "");
        vh.movePower.setText(move.power + "");
        vh.moveMovesLeft.setText(move.ppLeft + "");
        vh.moveMaxMoves.setText(move.pp + "");


        return convertView;
    }

    private static class ViewHolder {
        TextView moveName;
        TextView moveAccuracy;
        TextView movePower;
        TextView moveMovesLeft;
        TextView moveMaxMoves;

    }

}
