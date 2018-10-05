package com.example.adityapatel.note;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private NoteDataStore dataStore;
    GoogleMap mGoogleMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        supportMapFragment.getMapAsync(this);
        dataStore = NoteDataStoreImpl.sharedInstance(getApplicationContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mGoogleMap = googleMap;
        }
        showMarkers();
    }

    private void showMarkers() {
        double latitude;
        double longitude;
        String titleNote;
        for (int i = 0; i < dataStore.getNotes().size(); i++) {
            latitude = dataStore.getNotes().get(i).getLatitude();
            longitude = dataStore.getNotes().get(i).getLongitude();
            titleNote = dataStore.getNotes().get(i).getNote_name();
            LatLng noteCoordinates = new LatLng(latitude, longitude);
            Log.d("mapactiviy", noteCoordinates.toString());
            mGoogleMap.addMarker(new MarkerOptions().position(noteCoordinates).title(titleNote));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(noteCoordinates));
        }

    }

}



