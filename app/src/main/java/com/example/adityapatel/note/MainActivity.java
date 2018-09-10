package com.example.adityapatel.note;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String MyPreference = "MyPref";
    public static final String NoteCount = "noteKey";
    private ArrayList<NoteData> note = new ArrayList<>();
    public NoteDataStore dataStore;
    //private ArrayList<String> note_date = new ArrayList<>();
    //private ArrayList<String> note_text = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //dataStore = NoteDataStoreImpl.sharedInstance();
        //dataStore.getNotes();
        Log.d(TAG, "onCreate: hooooooooooooooooooooo");
        //ListView listView1 = findViewById(R.id.list1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, New_Note.class);
                startActivity(intent);
            }
        });

        initRecyclerView();

    }



   /* private void initData(){
        Log.d(TAG, "initData: initdata");
        Log.d(TAG, "onCreate:  Not null preference");
        String json = sharedPreferences.getString("noteKey","null");
        Log.d(TAG, "onCreate: "+ json);
            if(json == "null"){
                Log.d(TAG, "onCreate: json null");
                }
            else{
                //String json = sharedPreferences.getString(NoteCount,null);
                Log.d(TAG, "I am here");
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<NoteData>>(){}.getType();
                note = gson.fromJson(json,type);}
                Log.d(TAG, "initData: note size  " +note.size());
            // Log.d(TAG, "onCreate: GsonAfter"+note.get(0).toString());



    }*/
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview 12222");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);

    }
}
