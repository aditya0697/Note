package com.example.adityapatel.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;
import static com.example.adityapatel.note.New_Note.TAG;

public class NoteDataStoreImpl implements NoteDataStore {

    private static NoteDataStoreImpl sInstance;
    private  List<NoteData> dataList;
    private static String MyPreference = "MyPref";
    synchronized public static NoteDataStore sharedInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NoteDataStoreImpl(context);
        }
        return sInstance;
    }


    private NoteDataStoreImpl(Context context) {
        dataList = new ArrayList<>();

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
        }
    }

    @Override
    public void addNote(NoteData noteData) {
        dataList.add(noteData);
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

            return dataList;

    }

}
