package com.example.adityapatel.note;


import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class NoteData{
    private String userId;
    private String noteId;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    private String note_name;
    private String note_content;
    private String note_timestamp;
    private LatLng latLng;

    public NoteData(String userId,String note_name, String note_content, String note_timestamp, LatLng latLng) {
        this.userId = userId;
        this.note_name = note_name;
        this.note_content = note_content;
        this.note_timestamp = note_timestamp;
        this.latLng = latLng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public NoteData() {
    }

    public LatLng getLatLng() {

        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("note_title", note_name);
        result.put("note_content", note_content);
        result.put("note_timestamp", note_timestamp);
        result.put("note_latlng", latLng);

        return result;
    }

}
