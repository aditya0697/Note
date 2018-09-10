package com.example.adityapatel.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class New_Note extends AppCompatActivity {

    public static final String TAG = "ss";
    public static final String MyPreference = "MyPref";
    public static final String NoteCount = "noteKey";
    EditText edName, edNote;
    private NoteDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__note);
        edName = findViewById(R.id.name);
        edNote = findViewById(R.id.note);
        dataStore = NoteDataStoreImpl.sharedInstance();
        Log.d("newnote", dataStore.getNotes().toString());
        }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onback");
        dataStore.getNotes();
        // check if page 2 is open
        Toast.makeText(New_Note.this,"Back Press",Toast.LENGTH_SHORT).show();
        String name = edName.getText().toString();
        String note_content = edNote.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(note_content)){
            finish();
            return;
        }
        NoteData newNote = new NoteData(name,note_content,getTimeStamp());
        dataStore.addNote(newNote);
        Intent intent;
        intent = new Intent(New_Note.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            return true;
            }

        return super.onOptionsItemSelected(item);
    }


    public static String getTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        return currentDateTime;
        }
}
