package com.example.marti.pocketbattle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterView extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private static final String TAG = "LoginView";

    EditText username;
    EditText password;
    EditText email;
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        username = (EditText) findViewById(R.id.usernameRegField) ;
        password = (EditText) findViewById(R.id.passwordRegField);
        email = (EditText) findViewById(R.id.emailRegField);
        regButton = (Button) findViewById(R.id.registerRegButton);

        mAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener( event -> {
            createAccount(email.getText().toString(), password.getText().toString());
            startActivity(new Intent(RegisterView.this, LoginView.class));
        });
    }

    public void createAccount(String username, String password){
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterView.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
