package com.example.adityapatel.note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener, AllUserNoteConsumer<List<NoteData>>{

    private NoteDataStore dataStore;
    GoogleMap mGoogleMap;
<<<<<<< HEAD
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    List<NoteData> noteList = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
=======
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
    private static final float DEFAUL_ZOOM = 10f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        supportMapFragment.getMapAsync(this);
        dataStore = NoteDataStoreImpl.sharedInstance(getApplicationContext());
        dataStore.registerAllUserNoteSubject(this);
        dataStore.loadAllUserNotes();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mGoogleMap = googleMap;
            mGoogleMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        }
       // showMarkers(dataStore.getNotes());
    }

<<<<<<< HEAD
    private void showMarkers(List<NoteData> noteList) {
=======
    private void showMarkers() {
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
        double latitude = 0;
        double longitude = 0;

        String titleNote;

<<<<<<< HEAD
        for (int i = 0; i < noteList.size(); i++) {
            titleNote = noteList.get(i).getNote_name();
            latitude = noteList.get(i).getLatitude();
            longitude = noteList.get(i).getLongitude();
            LatLng latLng = new LatLng(latitude,longitude);
            if(noteList.get(i).getUserId().equals(currentUser.getUid())){
               Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(titleNote)
                        .snippet(noteList.get(i).getNote_content())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_note_marker)));
               marker.setTag(i);
               markers.add(marker);
            }else{
               Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(titleNote)
                        .snippet(noteList.get(i).getNote_content())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_note)));
               marker.setTag(i);
               markers.add(marker);
            }

=======
        for (int i = 0; i < dataStore.getNotes().size(); i++) {
            titleNote = dataStore.getNotes().get(i).getNote_name();
            latitude = dataStore.getNotes().get(i).getLatitude();
            longitude = dataStore.getNotes().get(i).getLongitude();
            LatLng latLng = new LatLng(latitude,longitude);

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(titleNote)
                    .snippet(dataStore.getNotes().get(i).getNote_content())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_note_marker)));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
        }
        if(noteList.size() == 0){
            LatLng denaliNationPark = new LatLng(63.129887, -151.197418);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(denaliNationPark, DEFAUL_ZOOM));
        }else {
<<<<<<< HEAD
            int last = noteList.size() - 1;
            latitude = noteList.get(last).getLatitude();
            longitude = noteList.get(last).getLongitude();
=======
            int last = dataStore.getNotes().size() - 1;
            latitude = dataStore.getNotes().get(last).getLatitude();
            longitude = dataStore.getNotes().get(last).getLongitude();
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
            LatLng last_note_location = new LatLng(latitude,longitude);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(last_note_location, DEFAUL_ZOOM));


<<<<<<< HEAD
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Object id = marker.getTag();
        int index = Integer.valueOf((int) id);
        String uId = currentUser.getUid();
        String uuId = noteList.get(index).getUserId();

        if(noteList.get(index).getUserId().equals(currentUser.getUid())){
            Intent intent = new Intent(this, EditNote.class);
            int notIndex = getNoteIndexOfUserNote(noteList.get(index).getNoteId());
            intent.putExtra("position", notIndex);
            this.startActivity(intent);
        }else{
            NoteData noteData1 = noteList.get(index);
            Intent intent = new Intent(this, EditNote.class);
            intent.putExtra("position",-1);
            intent.putExtra("Note_title",noteData1.getNote_name());
            intent.putExtra("Notedata_content",noteData1.getNote_content());
            this.startActivity(intent);
        }

      //  Toast.makeText(this,marker.getId(),Toast.LENGTH_SHORT).show();

    }

    private int getNoteIndexOfUserNote(String noteId){
        for(int i=0; i<dataStore.getNotes().size();i++){
            if(dataStore.getNotes().get(i).getNoteId().equals(noteId)){
                return i;
            }
=======
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
        }
        return -1;
    }


    @Override
    public void consume(List<NoteData> objects) {
        noteList = objects;
        showMarkers(objects);
    }

    /**
     * Create a new instance of the Parcelable class, instantiating it
     * from the given Parcel whose data had previously been written by
     * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
     *
     * @param source The Parcel to read the object's data from.
     * @return Returns a new instance of the Parcelable class.
     */

}



