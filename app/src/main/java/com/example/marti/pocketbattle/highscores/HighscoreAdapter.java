package com.example.marti.pocketbattle.highscores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marti.pocketbattle.R;
import com.example.marti.pocketbattle.models.User;

import java.util.List;

public class HighscoreAdapter extends ArrayAdapter<User> {

    public HighscoreAdapter(Context context, int resource, List<User> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HighscoreAdapter.ViewHolder vh;

        if (convertView == null ) {
            vh = new ViewHolder();
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.view_scoreslist_row, parent, false);
            vh.name = convertView.findViewById(R.id.usernameScore);
            vh.level = convertView.findViewById(R.id.levelScore);
            convertView.setTag(vh);
        } else {
            vh = (HighscoreAdapter.ViewHolder) convertView.getTag();
        }

        User user = getItem(position);
        vh.name.setText(user.username);
        vh.level.setText(user.currentXp + "");
        System.out.println("EXPRIENCE: " + user.currentXp + user.level + user.username);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        TextView level;
    }

}
