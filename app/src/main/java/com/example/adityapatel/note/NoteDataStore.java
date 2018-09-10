package com.example.adityapatel.note;

import android.content.Context;

import java.util.List;

public interface NoteDataStore {

    void addNote(NoteData noteData);

    void updateNote(int oldNoteToBeUpdated, NoteData newNote);

    void deleteNote(int noteToBeDeleted);

    void storeNote(Context context);

    List<NoteData> getNotes();

}
