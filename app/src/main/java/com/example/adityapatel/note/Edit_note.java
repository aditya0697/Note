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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edit_note extends AppCompatActivity  {

    private NoteDataStore dataStore;
    private double latitude;
    private double longitude;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        Log.d("dd","in here create");
        dataStore = NoteDataStoreImpl.sharedInstance(getApplicationContext());
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        int position = getIntent().getIntExtra("position",-1);
        editText2.setText(dataStore.getNotes().get(position).getNote_name());
        editText3.setText(dataStore.getNotes().get(position).getNote_content());
        IntentFilter intentFilter = new IntentFilter();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent != null) {
                    latitude = intent.getDoubleExtra("latitude", -1);
                    longitude = intent.getDoubleExtra("longitude", -1);
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    EditText editText2;
    EditText editText3;

    @Override
    public void onBackPressed() {
        String title = editText2.getText().toString();
        String body = editText3.getText().toString();
        String date = getTimeStamp();
        int position = getIntent().getIntExtra("position",-1);
        NoteData newNote = new NoteData(title,body,date, latitude, longitude );
        dataStore.updateNote(position,newNote);
       // dataStore.storeNotes(getApplicationContext());
        Toast.makeText(this,"Edited", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }


    public static String getTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        return currentDateTime;
    }



}
