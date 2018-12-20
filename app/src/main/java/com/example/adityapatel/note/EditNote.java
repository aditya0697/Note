package com.example.adityapatel.note;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditNote extends AppCompatActivity implements Serializable {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText edName;
    EditText edNote;
    private Bitmap imageBitmap;
    private LinearLayout mbackground;
    private NoteDataStore dataStore;
    private double latitude;
    private double longitude;
    private String noteId;
    private String noteTitle;
    private String noteContent;
    private String date;
    private List<String> imageIds;
    private String imageid;
    private List<String> imagePaths = new ArrayList<>();
    private NoteData noteData;
    private boolean imageFlag = false;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private boolean userNoteFalg = true;
    private int imageCount = -1;
    RecyclerView recyclerView;
    ImageRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        Log.d("dd","in here create");
        dataStore = NoteDataStoreImpl.sharedInstance(getApplicationContext());
        edName = findViewById(R.id.name);
        edNote = findViewById(R.id.note);
        mbackground = findViewById(R.id.note_background);
        init();

    }
    private void init(){
        int position = getIntent().getIntExtra("position",-1);
        if(position == -1){
            userNoteFalg = false;
            //noteData = (NoteData) getIntent().getParcelableExtra("NoteData");
            noteTitle = getIntent().getStringExtra("Note_title");
            noteContent = getIntent().getStringExtra("Notedata_content");
            edName.setText(noteTitle);
            edNote.setText(noteContent);
            edName.setFocusable(false);
            edName.setEnabled(false);
            edName.setCursorVisible(false);
            edName.setKeyListener(null);
            edName.setBackgroundColor(Color.TRANSPARENT);
            edNote.setFocusable(false);
            edNote.setEnabled(false);
            edNote.setCursorVisible(false);
            edNote.setKeyListener(null);

           // edNote.setBackgroundColor(Color.TRANSPARENT);

        }else {
            noteData = dataStore.getNotes().get(position);
            imageBitmap = noteData.getImage();
            noteContent = noteData.getNote_content();
            noteTitle = noteData.getNote_name();
            latitude = noteData.getLatitude();
            longitude = noteData.getLongitude();
            noteId = noteData.getNoteId();
            imageIds = noteData.getImageIds();
            imageCount = imageIds.size() -1;
            date = noteData.getNote_timestamp();
            edName.setText(noteData.getNote_name());
            edNote.setText(noteData.getNote_content());
            if( imageIds != null){
                for(int i=0; i< noteData.getImageIds().size();i++){
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir(noteData.getNoteId(), Context.MODE_PRIVATE);
                    File myImageFile = new File(directory, noteData.getImageIds().get(i)+".jpg");
                    if(myImageFile.exists()){
                        String path = myImageFile.getAbsolutePath();
                        imagePaths.add(path);
                    }else{
                        String path = noteData.getImagePaths().get(i);
                        imagePaths.add(path);
                    }

                }

                //Picasso.with(this).load(myImageFile).into(ivImage);
                initRecyclerView();
            }
            else if( noteData.getImagePaths() != null){
                imagePaths = noteData.getImagePaths();
                initRecyclerView();
            }
            initRecyclerView();
        }


    }
    private void initRecyclerView() {

        recyclerView = findViewById(R.id.note_images_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(EditNote.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new ImageRecyclerViewAdapter(imagePaths,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.addImagePath(null);
    }

    @SuppressLint("NewApi")
    private void setBackground(String path) {
        Bitmap imageBitmap = BitmapFactory.decodeFile(path);
        Drawable d = new BitmapDrawable(getResources(), imageBitmap);
        //edNote.setCompoundDrawables(d,null,null,null);
        //edNote.setCompoundDrawablesWithIntrinsicBounds(imageBitmap.describeContents(), 0, 0, 0);
        //int startIndex = edNote.getSelectionEnd();
        //SpannableStringBuilder ssb = new SpannableStringBuilder(edNote.getText()+" ");


        //ssb.setSpan(new ImageSpan(getApplicationContext(),imageBitmap, ImageSpan.ALIGN_BASELINE), startIndex+1, startIndex+2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //edNote.setText(ssb, TextView.BufferType.SPANNABLE);
        //mbackground.setBackground(imageBitmap);
        //edName.setAlpha(0.75f);
        //edNote.setAlpha(0.75f);
    }


    @Override
    public void onBackPressed() {
        if(userNoteFalg == true) {
            String title = edName.getText().toString();
            String body = edNote.getText().toString();
            int position = getIntent().getIntExtra("position", -1);
            //List<String> imagrIds = new ArrayList<String>(imageIds);
            if (noteTitle == null || noteContent == null) {
                dataStore.deleteNote(position);
            } else {
                if (!noteTitle.equals(title) || !noteContent.equals(body) || imageFlag) {
                    NoteData newNote = new NoteData(currentUser.getUid(), title, body, date, latitude, longitude, noteId, imageIds, imagePaths);
                    newNote.setNoteId(noteData.getNoteId());
                    dataStore.updateNote(position, newNote, imageFlag);
                }
            }
        }
       // dataStore.storeNotes(getApplicationContext());
        finish();

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

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @SuppressLint({"NewApi", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
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
            storeImage(imageBitmap);
            // edNote.setTextColor(R.color.white);
            //edName.setTextColor(R.color.white);
        }
    }

    private void storeImage(Bitmap imageBitmap){
        try {

            final File localFile = File.createTempFile(Integer.toString(imageCount), "jpeg");
            imageCount++;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(localFile);
                // Use the compress method on the BitMap object to write image to the OutputStream
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            noteData.setImagePath(localFile.getAbsolutePath());
            imagePaths.add(localFile.getAbsolutePath());
            imageFlag = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

*/
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

        if(resultCode != RESULT_CANCELED){
            adapter.addImagePath(imagePaths.get(imageCount));
            imageFlag = true;

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        final File directory = cw.getDir(noteData.getNoteId(), Context.MODE_PRIVATE);
        final File myImageFile = new File(directory, noteData.getImageIds().get(imageCount)+".jpg"); // Create image file
        if(myImageFile.exists()){
            String path = Long.toString(myImageFile.getTotalSpace());
        }
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "__"+Integer.toString(imageCount)+noteData.getNoteId();
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
