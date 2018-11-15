package com.example.adityapatel.note;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
=======
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
>>>>>>> f6a82e4013fdb2fbe1f55868640e32cadf2d37e0
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

<<<<<<< HEAD
import static android.text.TextUtils.isEmpty;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private NoteDataStore dataStore;
    private FirebaseAuth mAuth;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignin;
    private Button linkSignup;
    private ProgressBar mProgressBar;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkSignup = (Button) findViewById(R.id.link_signup);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mSignin = (Button) findViewById(R.id.btn_login);


        //initImageLoader();
        initProgressBar();
        setupFirebaseAuth();
        init();

    }




    private void init(){
        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(!isEmpty(mEmail.getText().toString())
                    && !isEmpty(mPassword.getText().toString())){
                Log.d(TAG, "onClick: attempting to authenticate.");

                showProgressBar();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),
                        mPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                hideProgressBar();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                });
            }else{
                Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        }
    });

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating to Register Screen");

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        hideSoftKeyboard();
    }


    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initProgressBar() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this,"User:"+user,Toast.LENGTH_SHORT).show();
                if (user != null) {

                    //check if email is verified
                    if (user.isEmailVerified()) {
                        Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
                // ...
            }
        };
    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {

    }
}

=======
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
>>>>>>> f6a82e4013fdb2fbe1f55868640e32cadf2d37e0
