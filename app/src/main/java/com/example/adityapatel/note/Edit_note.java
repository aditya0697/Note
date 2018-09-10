package com.example.adityapatel.note;

import android.content.Context;
import android.content.Intent;
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

    public static final String MyPreference = "MyPref";
    public static final String NoteCount = "noteKey";
    private NoteDataStore dataStore;
    SharedPreferences sharedPreferences;
    NoteData tempData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        Log.d("dd","in here create");
        dataStore = NoteDataStoreImpl.sharedInstance();
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        int position = getIntent().getIntExtra("position",-1);
        editText2.setText(dataStore.getNotes().get(position).getNote_name());
        editText3.setText(dataStore.getNotes().get(position).getNote_content());
        sharedPreferences = getSharedPreferences(MyPreference, Context.MODE_PRIVATE);

    }

    EditText editText2;
    EditText editText3;

    @Override
    public void onBackPressed() {
        String title = editText2.getText().toString();
        String body = editText3.getText().toString();
        String date = getTimeStamp();
        int position = getIntent().getIntExtra("position",-1);
        NoteData newNote = new NoteData(title,body,date);
        dataStore.updateNote(position,newNote);
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
