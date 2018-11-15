package com.example.adityapatel.note;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;
import static com.example.adityapatel.note.New_Note.TAG;

public class NoteDataStoreImpl implements NoteDataStore {

    private static NoteDataStoreImpl sInstance;
    private  static List<NoteData> dataList;
    private static String MyPreference = "MyPref";
    private static FirebaseAuth mAuth  ;
    private Context mcontext;
    private static List<NoteData> noteDataList = new ArrayList<>();
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase_post;
    private static DatabaseReference mDatabase_get = FirebaseDatabase.getInstance().getReference().child("user-notes").child(user.getUid()) ;
    private Activity activity;

    synchronized public static NoteDataStore sharedInstance()  {

        if (sInstance == null) {
            sInstance = new NoteDataStoreImpl();
            mAuth = FirebaseAuth.getInstance();
        }

        return sInstance;
    }



    private NoteDataStoreImpl() {
        loadNotes();
        /*
        try {
            Log.d(TAG, "NoteDataStoreImpl: Onstart datalist");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(MyPreference, null);
            if(json != null){
                Type type = new TypeToken<List<NoteData>>() {}.getType();
                dataList = gson.fromJson(json, type);
            }

        } catch (NullPointerException e) {
            Log.d(TAG, "NoteDataStoreImpl: Onstart datalist");
        }*/
    }

    public void loadNotes(){
        dataList = noteDataList;
        //activity = (Activity)context;
        mDatabase_get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String key = noteDataSnapshot.getKey();
                    List<String> stringList = new ArrayList<>();
                    String noteData = noteDataSnapshot.getValue().toString();
                    Map<String,String> map=(Map<String,String>)noteDataSnapshot.getValue();
                    String note_name = map.get("note_name");
                    String note_content = map.get("note_content");
                    String note_timestamp = map.get("note_timestamp");
                    String userId = map.get("userId");
                    DataSnapshot latlangSnapshot = (DataSnapshot) noteDataSnapshot.child("latLng");
                    Map<String,Double> latlngMap= (Map<String,Double>)latlangSnapshot.getValue() ;
                    // Double lat = Double.valueOf(latlngMap.get("latitude"));
                    //Double lan = Double.valueOf(latlngMap.get("longitude"));

                    LatLng latLng = new LatLng(37.340452,-121.8987795);
                    //  dataList.add( noteDataSnapshot.getValue(NoteData.class));
                    NoteData note = new NoteData(userId,note_name,note_content,note_timestamp,latLng);
                    dataList.add(note);
                    stringList.add(noteData);
                }
                for (Consumer subject : consumers) {
                    subject.consume(dataList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private final List<Consumer> consumers = new ArrayList<>();


    @Override
    public FirebaseUser getUser() {
        return user;
    }

    @Override
    public void logoutUser() {
        mAuth.signOut();
    }

    @Override
    public void setUser(FirebaseUser user1) {
        user = user1;
    }

    @Override
    public void setNoteList(List<NoteData> noteList) {

        int n = 0;

    }

    @Override
    public void addNote(NoteData noteData) {
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase_post.child("notes").push().getKey();


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + key, noteData);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + key, noteData);
        mDatabase_post.updateChildren(childUpdates);
        //dataList.add(noteData);
    }


    @Override
    public void updateNote(int oldNoteToBeUpdated, NoteData newNote) {

        dataList.remove(oldNoteToBeUpdated);
        dataList.add(oldNoteToBeUpdated, newNote);
    }

    @Override
    public void deleteNote(int noteTobeDeleted) {
        dataList.remove(noteTobeDeleted);
    }

    @Override
    public List<NoteData> getNotes() {
        final List<NoteData> noteList = new ArrayList<>();

            return dataList;

    }

    @Override
    public <T> void registerSubject(Consumer<T> consumer) {
        consumers.add(consumer);
    }

}
