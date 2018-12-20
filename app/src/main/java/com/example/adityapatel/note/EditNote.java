package com.example.adityapatel.note;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditNote extends AppCompatActivity  {

    private NoteDataStore dataStore;
    private double latitude;
    private double longitude;
    private String noteId;
    private String date;
    private BroadcastReceiver broadcastReceiver;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        Log.d("dd","in here create");
        dataStore = NoteDataStoreImpl.sharedInstance();
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        int position = getIntent().getIntExtra("position",-1);
        latitude = dataStore.getNotes().get(position).getLatitude();
        longitude = dataStore.getNotes().get(position).getLongitude();
        noteId = dataStore.getNotes().get(position).getNoteId();
        date = dataStore.getNotes().get(position).getNote_timestamp();
        editText2.setText(dataStore.getNotes().get(position).getNote_name());
        editText3.setText(dataStore.getNotes().get(position).getNote_content());
        IntentFilter intentFilter = new IntentFilter();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    EditText editText2;
    EditText editText3;

    @Override
    public void onBackPressed() {
        String title = editText2.getText().toString();
        String body = editText3.getText().toString();
        int position = getIntent().getIntExtra("position",-1);
        NoteData newNote = new NoteData(currentUser.getUid(),title,body,date, latitude,longitude,noteId);
        dataStore.updateNote(position,newNote);
       // dataStore.storeNotes(getApplicationContext());
        finish();

    }


}
