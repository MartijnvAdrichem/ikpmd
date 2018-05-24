package com.example.marti.pocketbattle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class HomeScreenView extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView hsText;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_view);
        mAuth = FirebaseAuth.getInstance();
        hsText = (TextView) findViewById(R.id.hsText);
        currentUser = mAuth.getCurrentUser();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
        if (currentUser != null) {
            hsText.setText(currentUser.getEmail().toString());
        }

    }
}
