package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marti.pocketbattle.models.User;
import com.example.marti.pocketbattle.shop.ShopView;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileView extends AppCompatActivity {

    private PieChart winsChart;
    private PieChart damageChart;
    private TextView username;
    private TextView level;
    private ProgressBar levelBar;
    private Button shopButton;

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
        shopButton = findViewById(R.id.ProfileShopBT);

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

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        shopButton.setOnClickListener(e -> {
            startActivity(new Intent(ProfileView.this, ShopView.class));
        });
    }
}
