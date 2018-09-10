package com.example.adityapatel.note;


import android.os.Parcel;
import android.os.Parcelable;

public class NoteData implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.note_name);
        dest.writeString(this.note_content);
        dest.writeString(this.note_timestamp);

    }

    private NoteData(Parcel in) {
        String noteName = in.readString();
        String noteContent  = in.readString();
        String timeStamp = in.readString();
    }


    public static final Parcelable.Creator<NoteData> CREATOR = new Parcelable.Creator<NoteData>(){

        @Override
        public NoteData createFromParcel(Parcel source) {
            return new NoteData(source);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };


}
