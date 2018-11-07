package com.example.adityapatel.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "EmailPassword";
    private NoteDataStore dataStore;
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button signin;
    private TextView linkSignup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        email = (EditText)view.findViewById(R.id.input_email);
        password = (EditText)view.findViewById(R.id.input_password);
        signin = (Button)view.findViewById(R.id.btn_login);
        linkSignup = (TextView)view.findViewById(R.id.link_signup);
        dataStore = NoteDataStoreImpl.sharedInstance(getContext());
        mAuth = FirebaseAuth.getInstance();
        linkSignup.setOnClickListener(this);
        signin.setOnClickListener(this);
        return view;
    }

    private boolean validateForm() {
        boolean valid = true;

        String emailid = email.getText().toString();
        if (TextUtils.isEmpty(emailid)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String passwordField = password.getText().toString();
        if (TextUtils.isEmpty(passwordField)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_login){
            signIn(email.getText().toString(),password.getText().toString());
        }else if (id == linkSignup.getId()){
            createAccount(email.getText().toString(),password.getText().toString());
        }

    }

    private void createAccount(String email, String password) {

        Log.d(TAG, "createAccount: "+email);
        if(!validateForm()){
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:    success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    dataStore.setUser(user);
                    updateUI(user);
                }
                else{
                    Log.d(TAG, "createUserWithEmail:failure ",task.getException());
                    Toast.makeText(getContext(),"Authentication failed.",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                hideProgressDialog();
            }
        });



    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn: "+email);
        if(!validateForm()){
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail: Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    dataStore.setUser(user);
                    updateUI(user);
                }else {
                    Log.d(TAG, "signInWithEmail: Failed ");
                    updateUI(null);
                }

                if(!task.isSuccessful()){
                    Toast.makeText(getContext(),"Authentication failed.",Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if(user != null){
            getActivity().getSupportFragmentManager().beginTransaction().detach( new LoginFragment()).commit();
            Intent intent = new Intent(getContext(), MainActivity.class);
            getContext().startActivity(intent);
        }
    }
}
