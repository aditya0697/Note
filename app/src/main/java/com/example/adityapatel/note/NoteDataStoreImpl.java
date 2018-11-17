package com.example.adityapatel.note;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDataStoreImpl implements NoteDataStore {

    private static NoteDataStoreImpl sInstance;
    private  static List<NoteData> dataList;
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;
    private static DatabaseReference mDatabase_post;
    private static DatabaseReference mDatabase_get;


    synchronized public static NoteDataStore sharedInstance()  {

        if (sInstance == null) {

            sInstance = new NoteDataStoreImpl();
            mAuth = FirebaseAuth.getInstance();
        }

        return sInstance;
    }
    public void clear(){
        mDatabase_post = null;
        mDatabase_get = null;
        user = null;
        sInstance = null;
    }

    private void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        mDatabase_get = FirebaseDatabase.getInstance().getReference().child("user-notes").child(user.getUid()) ;
    }

    private NoteDataStoreImpl() {
        init();
        dataList = new ArrayList<>();
        //activity = (Activity)context;
        mDatabase_get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String key = noteDataSnapshot.getKey();
                    List<String> stringList = new ArrayList<>();
                    String noteData = noteDataSnapshot.getValue().toString();
                    Map<String,String> map=(Map<String,String>)noteDataSnapshot.getValue();
                    String note_name = map.get("note_name");
                    String note_content = map.get("note_content");
                    String note_timestamp = map.get("note_timestamp");
                    String userId = map.get("userId");
                    Double latitude = Double.valueOf(map.get("latitude"));
                    Double longitude = Double.valueOf(map.get("longitude"));
                    //dataList.add( noteDataSnapshot.getValue(NoteData.class));
                    NoteData note = new NoteData(userId,note_name,note_content, note_timestamp, latitude, longitude);
                    dataList.add(note);
                    stringList.add(noteData);
                }*/

                dataList.clear();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NoteData note= noteDataSnapshot.getValue(NoteData.class);
                            dataList.add(note);
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

  /* public void loadNotes(){
        mDatabase_get.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String key = noteDataSnapshot.getKey();
                    List<String> stringList = new ArrayList<>();
                    String noteData = noteDataSnapshot.getValue().toString();
                    Map<String,String> map=(Map<String,String>)noteDataSnapshot.getValue();
                    String note_name = map.get("note_name");
                    String note_content = map.get("note_content");
                    String note_timestamp = map.get("note_timestamp");
                    String userId = map.get("userId");
                    Double latitude = Double.valueOf(map.get("latitude"));
                    Double longitude = Double.valueOf(map.get("longitude"));
                    //dataList.add( noteDataSnapshot.getValue(NoteData.class));
                    NoteData note = new NoteData(userId,note_name,note_content, note_timestamp, latitude, longitude);
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

    }*/

    private final List<Consumer> consumers = new ArrayList<>();


    @Override
    public FirebaseUser getUser() {
        return user;
    }

    @Override
    public void logoutUser() {
        mAuth.signOut();
        dataList.clear();


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
        String key = mDatabase_post.child("notes").push().getKey();
        noteData.setNoteId(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + key, noteData);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + key, noteData);
        mDatabase_post.updateChildren(childUpdates);
        dataList.add(noteData);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }


    @Override
    public void updateNote(int oldNoteToBeUpdated, NoteData noteData) {
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + noteData.getNoteId(), noteData);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), noteData);
        mDatabase_post.updateChildren(childUpdates);
        dataList.add(noteData);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }

    @Override
    public void deleteNote(int index) {
        NoteData noteData = dataList.get(index);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + noteData.getNoteId(), null);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), null);
        mDatabase_post.updateChildren(childUpdates);
        dataList.remove(index);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }

    @Override
    public List<NoteData> getNotes() {
            return dataList;
    }

    @Override
    public <T> void registerSubject(Consumer<T> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void load_notes() {

    }

}
