package com.example.adityapatel.note;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteDataStoreImpl implements NoteDataStore {

    private static NoteDataStoreImpl sInstance;
    private  static List<NoteData> dataList;
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;
    private static DatabaseReference mDatabase_post;
    private static DatabaseReference mDatabase_get;
    private StorageReference mStorageRef;
    final List<ImageStoreStructure> image_note_store = new ArrayList<>();
//    final List<String> image_path = new ArrayList<>();


    synchronized public static NoteDataStore sharedInstance()  {

        if (sInstance == null) {

            sInstance = new NoteDataStoreImpl();
            mAuth = FirebaseAuth.getInstance();
        }

        return sInstance;
    }
    public void clear(){
        mDatabase_post = null;
        mDatabase_get = null;
        user = null;
        sInstance = null;
    }

    private void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        mDatabase_get = FirebaseDatabase.getInstance().getReference().child("user-notes").child(user.getUid()) ;

    }

    private NoteDataStoreImpl() {
        init();
        dataList = new ArrayList<>();
        mDatabase_get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String key = noteDataSnapshot.getKey();
                    List<String> stringList = new ArrayList<>();
                    String noteData = noteDataSnapshot.getValue().toString();
                    Map<String,String> map=(Map<String,String>)noteDataSnapshot.getValue();
                    String note_name = map.get("note_name");
                    String note_content = map.get("note_content");
                    String note_timestamp = map.get("note_timestamp");
                    String userId = map.get("userId");
                    Double latitude = Double.valueOf(map.get("latitude"));
                    Double longitude = Double.valueOf(map.get("longitude"));
                    //dataList.add( noteDataSnapshot.getValue(NoteData.class));
                    NoteData note = new NoteData(userId,note_name,note_content, note_timestamp, latitude, longitude);
                    dataList.add(note);
                    stringList.add(noteData);
                }*/

                dataList.clear();

                int count = 0;
                int imageCount = 0;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NoteData note= noteDataSnapshot.getValue(NoteData.class);
                    dataList.add(note);

                    if(note.getImageId() != null) {
                        int isImageAlreadyStore = searchImageId(note.getNoteId());
                        if (isImageAlreadyStore != -1) {
                            try {
                                downloadImage(isImageAlreadyStore);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ImageStoreStructure imageStoreStructure = new ImageStoreStructure(count, note.getNoteId(), null);
                            image_note_store.add(imageStoreStructure);
                            //image_note_id.add(note.getImageId());
                            try {
                                downloadImage(image_note_store.size() - 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                for (Consumer subject : consumers) {
                    subject.consume(dataList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private final List<Consumer> consumers = new ArrayList<>();


    @Override
    public FirebaseUser getUser() {
        return user;
    }

    @Override
    public void logoutUser() {
        mAuth.signOut();
        dataList.clear();


    }

    @Override
    public void setUser(FirebaseUser user1) {
        user = user1;
    }

    @Override
    public void setNoteList(List<NoteData> noteList) {

        int n = 0;

    }

    @Override
    public void addNote(NoteData noteData) {
        if(noteData.getImagePath() == null){
            String key = mDatabase_post.child("notes").push().getKey();
            noteData.setNoteId(key);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/notes/" + key, noteData);
            childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + key, noteData);
            mDatabase_post.updateChildren(childUpdates);
            dataList.add(noteData);
        }else {
            String key = mDatabase_post.child("notes").push().getKey();
            noteData.setNoteId(key);
            noteData.setImageId(key);
            uploadImage(noteData);
            noteData.setImage(null);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/notes/" + key, noteData);
            childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + key, noteData);
            mDatabase_post.updateChildren(childUpdates);
            dataList.add(noteData);
        }

        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }


    @Override
    public void updateNote(int oldNoteToBeUpdated, NoteData noteData, boolean imageFlag) {
        if(imageFlag){
            uploadImage(noteData);
        }
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + noteData.getNoteId(), noteData);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), noteData);
        mDatabase_post.updateChildren(childUpdates);
        dataList.remove(oldNoteToBeUpdated);
        dataList.add(oldNoteToBeUpdated,noteData);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }

    @Override
    public void deleteNote(int index) {
        NoteData noteData = dataList.get(index);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + noteData.getNoteId(), null);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), null);
        mDatabase_post.updateChildren(childUpdates);
        dataList.remove(index);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }

    @Override
    public List<NoteData> getNotes() {
            return dataList;
    }

    @Override
    public <T> void registerSubject(Consumer<T> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void load_notes() {

    }

    private void uploadImage(final NoteData noteData){
        String noteId = noteData.getNoteId();
        final Bitmap bitmap = BitmapFactory.decodeFile(noteData.getImagePath());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("note-images").child(noteData.getNoteId()+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final boolean[] flag = {false};
        UploadTask uploadTask = mStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                int i=0;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc
                flag[0] = true;

            }
        });

        try {
            final File localFile = File.createTempFile(noteData.getImageId(), "jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(localFile);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        //noteData.setImagePath(null);
    }

    private void downloadImage(final int index) throws IOException {

        if(image_note_store.get(index).getImagePath() != null){

            dataList.get(index).setImagePath(image_note_store.get(index).getImagePath());
        }
        else {
            mStorageRef = FirebaseStorage.getInstance().getReference().child("note-images").child(image_note_store.get(index).getId()+".jpg");

            final File localFile = File.createTempFile(image_note_store.get(index).getNoteID(), "jpg");
            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    // Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    image_note_store.get(index).setImagePath(localFile.getAbsolutePath());
                    dataList.get(image_note_store.get(index).getId()).setImagePath(localFile.getAbsolutePath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }


    }

    private int searchImageId(String noteId){
        for (int i=0; i< image_note_store.size(); i++){
            if(image_note_store.get(i).getNoteID().equals(noteId)){
                return i;
            }
        }

        return -1;
    }

}






 /* public void loadNotes(){
        mDatabase_get.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String key = noteDataSnapshot.getKey();
                    List<String> stringList = new ArrayList<>();
                    String noteData = noteDataSnapshot.getValue().toString();
                    Map<String,String> map=(Map<String,String>)noteDataSnapshot.getValue();
                    String note_name = map.get("note_name");
                    String note_content = map.get("note_content");
                    String note_timestamp = map.get("note_timestamp");
                    String userId = map.get("userId");
                    Double latitude = Double.valueOf(map.get("latitude"));
                    Double longitude = Double.valueOf(map.get("longitude"));
                    //dataList.add( noteDataSnapshot.getValue(NoteData.class));
                    NoteData note = new NoteData(userId,note_name,note_content, note_timestamp, latitude, longitude);
                    dataList.add(note);
                    stringList.add(noteData);
                }
                for (Consumer subject : consumers) {
                    subject.consume(dataList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
