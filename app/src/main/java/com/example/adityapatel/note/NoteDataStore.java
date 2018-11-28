package com.example.adityapatel.note;

import android.content.Context;
import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface NoteDataStore {

    FirebaseUser getUser();
    void logoutUser();
    void setUser(FirebaseUser user);
    void addNote(NoteData noteData);

    void updateNote(int oldNoteToBeUpdated, NoteData newNote, boolean imageFlag);

    void deleteNote(int noteToBeDeleted);
    void setNoteList(List<NoteData> noteList);
    List<NoteData> getNotes();

    <T> void registerSubject(Consumer<T> consumer);
    void load_notes();
    void clear();

}
