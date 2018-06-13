package com.example.marti.pocketbattle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marti.pocketbattle.models.User;
import com.example.marti.pocketbattle.pokemon.PokemonList;
import com.example.marti.pocketbattle.shop.ShopView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileView extends AppCompatActivity {

    private PieChart winsChart;
    private PieChart damageChart;
    private TextView username;
    private TextView level;
    private TextView winsChartTV;
    private TextView damageChartTV;
    private ProgressBar levelBar;
    private Button pokemonButton;

    private DatabaseReference userRef;
    public static User user;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        winsChart = findViewById(R.id.winsChart);
        damageChart = findViewById(R.id.damageChart);
        username = findViewById(R.id.ProfileUsernameTV);
        level = findViewById(R.id.ProfileLevelTV);
        levelBar = findViewById(R.id.ProfileProgressBar);
        pokemonButton = findViewById(R.id.MyPokemonBT);
        winsChartTV = findViewById(R.id.tvWinsChart);
        damageChartTV = findViewById(R.id.tvDamageChart);

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userRef = db.getReference("users/" + mAuth.getCurrentUser().getUid());

    }

    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        user = d.getValue(User.class);
                    }
                    username.setText(user.username);
                    level.setText("Level "+user.level);
                    levelBar.setMax(user.nextLevelXp);
                    levelBar.setProgress(user.currentXp);
                    setChartsData(winsChart, user.wins, user.loses);
                    setChartsData(damageChart, user.damageDone, user.damageTaken);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        pokemonButton.setOnClickListener(e -> {
            startActivity(new Intent(ProfileView.this, PokemonList.class));
        });
    }

    public void setChartsData(PieChart chart, int value1, int value2){

        winsChartTV.setText("W: "+user.wins + "\nL: " + user.loses);
        damageChartTV.setText("D: " + user.damageDone + "\nT: "+user.damageTaken);

        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.setDrawSliceText(true);
        chart.getLegend().setEnabled(false);
        chart.setHoleRadius(45);
        chart.setUsePercentValues(true);

        ArrayList<Entry> yValuesWins = new ArrayList<>();
        ArrayList<String> xValuesWins = new ArrayList<>();

        yValuesWins.add(new Entry(value1, 0));
        xValuesWins.add("");

        yValuesWins.add(new Entry(value2, 1));
        xValuesWins.add("");

        ArrayList<Integer> colors = new ArrayList<>();
        if (value1 > 0) {
            colors.add(Color.rgb(0,255,0));
        }
        colors.add(Color.rgb(255,0,0));

        PieDataSet dataSet = new PieDataSet(yValuesWins, "");
        dataSet.setColors(colors);

        PieData data = new PieData(xValuesWins, dataSet);
        chart.setData(data);
        chart.invalidate();

    }
}
