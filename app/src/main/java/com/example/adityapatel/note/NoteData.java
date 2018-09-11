package com.example.adityapatel.note;


import android.os.Parcel;
import android.os.Parcelable;

public class NoteData {
    private String note_name;
    private String note_content;
    private String note_timestamp;

    public NoteData(String note_name, String note_content, String note_timestamp) {
        this.note_name = note_name;
        this.note_content = note_content;
        this.note_timestamp = note_timestamp;
    }

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }

    public String getNote_timestamp() {
        return note_timestamp;
    }

    public void setNote_timestamp(String note_timestamp) {
        this.note_timestamp = note_timestamp;
    }

}
