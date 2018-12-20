package com.example.adityapatel.note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewNote extends AppCompatActivity {

    public static final String TAG = "ss";

    EditText edName, edNote;
    private LinearLayout mbackground;
    private static final int REQUEST_IMAGE_CAPTURE = 10;
    private NoteDataStore dataStore;
    private static final int LOCTION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted = false;
    private static LatLng currentlatlng;
    private String imageid;
  //  private NoteLocation noteLocation = null;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mbound = false;
    private Location mlocation;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private List<String> imageIds = new ArrayList<String>();
    private List<String> imagePaths = new ArrayList<String>();
    private int imageCount=-1;

    RecyclerView recyclerView;
    ImageRecyclerViewAdapter adapter;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            NoteLocation.LocalBinder localBinder = (NoteLocation.LocalBinder) service;
            mbound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_note);
        super.onCreate(savedInstanceState);
        edName = findViewById(R.id.name);
        edNote = findViewById(R.id.note);
        dataStore = NoteDataStoreImpl.sharedInstance(getApplicationContext());
        mbackground = findViewById(R.id.note_background);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        initRecyclerView();
        getLocationPermission();
        //getDeviceLocation();


    }

    private void initRecyclerView() {

        recyclerView = findViewById(R.id.note_images_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(NewNote.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new ImageRecyclerViewAdapter(imagePaths,getApplicationContext());
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void goToDisplayImage(int index){
        //imageBitmap =  BitmapFactory.decodeFile(imagePath);
        if(imagePaths.size() != 0){
            Intent i = new Intent(this, DisplayImageActivity.class);
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //byte[] byteArray = stream.toByteArray();
            i.putExtra("picture",imagePaths.get(index));
            startActivity(i);
        }

    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location ) task.getResult();
                            currentlatlng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                        }else {
                            Log.d(TAG, "onComplete: Not found location");
                            currentlatlng = new LatLng(63.129887, -151.197418);
                            Toast.makeText(NewNote.this,"unable to get current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: Exception "+e.getMessage());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onback");
        Toast.makeText(NewNote.this, "Back Press", Toast.LENGTH_SHORT).show();
        String name = edName.getText().toString();
        String note_content = edNote.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(note_content)) {
            finish();
            return;
        }
        NoteData newNote = new NoteData(currentFirebaseUser.getUid(),name, note_content, getTimeStamp(), currentlatlng.latitude, currentlatlng.longitude,null,imageIds, imagePaths);
        dataStore.addNote(newNote);
        finish();
        /*Intent intent;
        intent = new Intent(NewNote.this, MainActivity.class);
        startActivity(intent);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        //menu.findItem(R.id.action_favorite).setVisible(false);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cemara) {
            dispatchTakePictureIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String currentDateTime = dateFormat.format(new Date());
        return currentDateTime;
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                getDeviceLocation();

            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCTION_PERMISSION_REQUEST_CODE);

            }

        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCTION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCTION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                    //initialize map
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void dispatchTakePictureIntent() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                imagePaths.add(photoFile.getAbsolutePath());
                imageCount++;
                imageid = "__"+Integer.toString(imageCount);
                imageIds.add(imageid);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


            }
        }

    }
    @SuppressLint({"NewApi", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            Drawable d = new BitmapDrawable(getResources(), imageBitmap);

            //edNote.setCompoundDrawables(d,null,null,null);
            //edNote.setCompoundDrawablesWithIntrinsicBounds(imageBitmap.describeContents(), 0, 0, 0);
            //int startIndex = edNote.getSelectionEnd();
            //SpannableStringBuilder ssb = new SpannableStringBuilder(edNote.getText()+" ");


            //ssb.setSpan(new ImageSpan(getApplicationContext(),imageBitmap, ImageSpan.ALIGN_BASELINE), startIndex+1, startIndex+2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            //edNote.setText(ssb, TextView.BufferType.SPANNABLE);
            mbackground.setBackground(d);
            edNote.setAlpha(0.75f);
            edName.setAlpha(0.75f);
            //storeImage(imageBitmap,"newNoteImage");
           // edNote.setTextColor(R.color.white);
            //edName.setTextColor(R.color.white);
        }*/
        if(resultCode != RESULT_CANCELED){
            adapter.addImagePath(imagePaths.get(imageCount));
            //imageBitmap =  BitmapFactory.decodeFile(imagePath);
            //imageBitmap.extractAlpha();
            //Drawable d = new BitmapDrawable(getResources(), imageBitmap);
            //mbackground.setBackground(d);
          //  edNote.setAlpha(0.75f);
            //edName.setAlpha(0.75f);
        }
    }
/*
    private void storeImage(Bitmap imageBitmap, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 500, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imagePath = mypath.getAbsolutePath();
    }*/

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "NOTE_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //imagePaths.add(image.getAbsolutePath());
        return image;
    }
}
