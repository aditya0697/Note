package com.example.adityapatel.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String loginEmailString;
    String loginPasswordString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText loginEmail = findViewById(R.id.loginEmail);
        EditText loginPassword = findViewById(R.id.loginPassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();
        loginEmailString = loginEmail.getText().toString();
        loginPasswordString = loginPassword.getText().toString();
        signInUser(loginEmailString, loginPasswordString);

    }

    public void signInUser(String loginEmailStr, String loginPasswordStr) {

        if(mAuth.getCurrentUser() != null) {
            mAuth.signInWithEmailAndPassword(loginEmailStr, loginPasswordStr )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            sendToMain();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Authentication Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private void sendToMain() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


}
