package com.example.adityapatel.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;

public class NoteDataStoreImpl implements NoteDataStore {

    private static NoteDataStoreImpl sInstance;
    private final List<NoteData> dataList;

    synchronized public static NoteDataStore sharedInstance() {
        if (sInstance == null) {
            sInstance = new NoteDataStoreImpl();
        }
        return sInstance;
    }

    private NoteDataStoreImpl() {
        dataList = new ArrayList<>();
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

    @Override
    public void storeNote(Context context) {
        /*Set<String> noteSet =*/
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("noteTitle", dataList.toString());
    }

    void dddd() {}
}
