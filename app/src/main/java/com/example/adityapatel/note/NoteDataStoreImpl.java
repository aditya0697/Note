package com.example.adityapatel.note;

<<<<<<< HEAD
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
=======
import android.support.annotation.NonNull;

>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
<<<<<<< HEAD
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
=======

>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD

import static com.example.adityapatel.note.NewNote.TAG;
=======
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae

public class NoteDataStoreImpl implements NoteDataStore {

    private static NoteDataStoreImpl sInstance;
<<<<<<< HEAD
    private static List<NoteData> dataList;
    private static List<NoteData> allUserNoteList;
=======
    private  static List<NoteData> dataList;
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;
    private static DatabaseReference mDatabase_post;
    private static DatabaseReference mDatabase_get;
<<<<<<< HEAD
    private static DatabaseReference mAllUserDatabaseReference;

    private StorageReference mStorageRef;
    final List<ImageStoreStructure> image_note_store = new ArrayList<>();
    Context mcontext;
//    final List<String> image_path = new ArrayList<>();


    synchronized public static NoteDataStore sharedInstance(Context context) {

        if (sInstance == null) {

            sInstance = new NoteDataStoreImpl(context);
=======


    synchronized public static NoteDataStore sharedInstance()  {

        if (sInstance == null) {

            sInstance = new NoteDataStoreImpl();
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
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

<<<<<<< HEAD
    public void clear() {
        mDatabase_post = null;
        mDatabase_get = null;
        user = null;
        sInstance = null;
    }

    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        mDatabase_get = FirebaseDatabase.getInstance().getReference().child("iuser-notes").child(user.getUid());
        mAllUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("inotes");

    }

    private int getNoteIndex(String noteId) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getNoteId().equals(noteId)) {
                return i;
            }
        }
        return -1;
    }

    private NoteDataStoreImpl(Context context) {
        this.mcontext = context;
        init();
        dataList = new ArrayList<>();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                NoteData note = dataSnapshot.getValue(NoteData.class);
                dataList.add(note);
                try {
                    downloadImage(note);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (Consumer subject : consumers) {
                    subject.consume(dataList);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                NoteData note = dataSnapshot.getValue(NoteData.class);
                int index = getNoteIndex(note.getNoteId());
                dataList.remove(index);
                dataList.add(index, note);
                try {
                    downloadImage(note);
                } catch (IOException e) {
                    e.printStackTrace();
=======
    private void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        mDatabase_get = FirebaseDatabase.getInstance().getReference().child("user-notes").child(user.getUid()) ;
    }

    private NoteDataStoreImpl() {
        init();
        dataList = new ArrayList<>();
        //activity = (Activity)context;
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
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NoteData note= noteDataSnapshot.getValue(NoteData.class);
                            dataList.add(note);
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
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
                }
                for (Consumer subject : consumers) {
                    subject.consume(dataList);
                }
            }


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }
<<<<<<< HEAD

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };

        mDatabase_get.addChildEventListener(childEventListener);
    }
=======
        });

    }*/
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae


    private final List<Consumer> consumers = new ArrayList<>();
    private final List<AllUserNoteConsumer> allUserNoteConsumers = new ArrayList<>();
    //private AllUserNoteConsumer<List<NoteData>> allUserNoteConsumer;

    @Override
    public FirebaseUser getUser() {
        return user;
    }

    @Override
    public void logoutUser() {
        mAuth.signOut();
        dataList.clear();
<<<<<<< HEAD
=======


>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
    }

    @Override
    public void setUser(FirebaseUser user1) {
        user = user1;
    }

    @Override
    public void setNoteList(List<NoteData> noteList) {
    }

    @Override
    public void addNote(NoteData noteData) {
<<<<<<< HEAD
        if (noteData.getImageIds().size() == 0) {
            String key = mDatabase_post.child("inotes").push().getKey();
            noteData.setNoteId(key);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/inotes/" + key, noteData);
            childUpdates.put("/iuser-notes/" + noteData.getUserId() + "/" + key, noteData);
            mDatabase_post.updateChildren(childUpdates);
            //dataList.add(noteData);
        } else {
            String key = mDatabase_post.child("inotes").push().getKey();
            noteData.setNoteId(key);
            //noteData.setImageId();
            for (int i = 0; i < noteData.getImagePaths().size(); i++) {
                String id = noteData.getImageIds().get(i) + key;
                noteData.setImageId(id, i);
            }
            //NoteData tempNote = noteData;
            //tempNote.setImagePaths(null);
            uploadImage(noteData);
            //noteData.setImage(null);
            //noteData.setImagePaths(null);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/inotes/" + key, noteData);
            childUpdates.put("/iuser-notes/" + noteData.getUserId() + "/" + key, noteData);
            mDatabase_post.updateChildren(childUpdates);
            //noteData.setImagePaths(imagepaths);
            // dataList.add(noteData);
=======
        String key = mDatabase_post.child("notes").push().getKey();
        noteData.setNoteId(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + key, noteData);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + key, noteData);
        mDatabase_post.updateChildren(childUpdates);
        dataList.add(noteData);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
        }
    }


    @Override
<<<<<<< HEAD
    public void updateNote(int oldNoteToBeUpdated, NoteData noteData, boolean imageFlag) {
        if (imageFlag) {
            String key = noteData.getNoteId();
            for (int i = 0; i < noteData.getImagePaths().size(); i++) {
                noteData.setImageId(key + Integer.toString(i), i);
            }
            uploadImage(noteData);
        }

        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        mDatabase_post.child("inotes").child(noteData.getNoteId()).setValue(noteData);
        mDatabase_post.child("iuser-notes").child(noteData.getUserId()).child(noteData.getNoteId()).setValue(noteData);
        /*Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/inotes/" + noteData.getNoteId(), noteData);
        childUpdates.put("/iuser-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), noteData);
        mDatabase_post.updateChildren(childUpdates);*/
        dataList.remove(oldNoteToBeUpdated);
        dataList.add(oldNoteToBeUpdated, noteData);
=======
    public void updateNote(int oldNoteToBeUpdated, NoteData noteData) {
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + noteData.getNoteId(), noteData);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), noteData);
        mDatabase_post.updateChildren(childUpdates);
        dataList.add(noteData);
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }

    @Override
    public void deleteNote(int index) {
        NoteData noteData = dataList.get(index);
<<<<<<< HEAD
        mDatabase_post = FirebaseDatabase.getInstance().getReference();
        mDatabase_post.child("inotes").child(noteData.getNoteId()).setValue(null);
        mDatabase_post.child("iuser-notes").child(noteData.getUserId()).child(noteData.getNoteId()).setValue(null);
        /*Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/inotes/" + noteData.getNoteId(), null);
        childUpdates.put("/iuser-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), null);
        mDatabase_post.updateChildren(childUpdates);*/
=======
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/notes/" + noteData.getNoteId(), null);
        childUpdates.put("/user-notes/" + noteData.getUserId() + "/" + noteData.getNoteId(), null);
        mDatabase_post.updateChildren(childUpdates);
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
        dataList.remove(index);
        for (Consumer subject : consumers) {
            subject.consume(dataList);
        }
    }

    @Override
    public List<NoteData> getNotes() {
<<<<<<< HEAD
        return dataList;
=======
            return dataList;
>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
    }

    @Override
    public <T> void registerSubject(Consumer<T> consumer) {
        consumers.add(consumer);

    }

    @Override
    public <T> void registerAllUserNoteSubject(AllUserNoteConsumer<T> allUserNoteConsumer) {
        allUserNoteConsumers.add(allUserNoteConsumer);
    }

    @Override
    public void load_notes() {
        mDatabase_get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();

                int count = 0;
                int imageCount = 0;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NoteData note = noteDataSnapshot.getValue(NoteData.class);
                    dataList.add(note);

                    if (note.getImageIds().size() != 0) {
                        int isImageAlreadyStore = searchImageId(note.getNoteId());
                        if (isImageAlreadyStore != -1) {
                            try {
                                downloadImage(note);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ImageStoreStructure imageStoreStructure = new ImageStoreStructure(count, note.getNoteId(), null);
                            image_note_store.add(imageStoreStructure);
                            //image_note_id.add(note.getImageId());
                            try {
                                downloadImage(note);
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
    public void loadAllUserNotes() {
        mAllUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUserNoteList = new ArrayList<>();

                int count = 0;
                int imageCount = 0;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    NoteData note = noteDataSnapshot.getValue(NoteData.class);
                    allUserNoteList.add(note);

                    if (note.getImageIds().size() != 0) {
                        int isImageAlreadyStore = searchImageId(note.getNoteId());
                        if (isImageAlreadyStore != -1) {
                            try {
                                downloadImage(note);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ImageStoreStructure imageStoreStructure = new ImageStoreStructure(count, note.getNoteId(), null);
                            image_note_store.add(imageStoreStructure);
                            //image_note_id.add(note.getImageId());
                            try {
                                downloadImage(note);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                for (AllUserNoteConsumer subject : allUserNoteConsumers) {
                    subject.consume(allUserNoteList);
                }
               // AllUserNoteConsumer.consume(allUserNoteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(final NoteData noteData) {
        String noteId = noteData.getNoteId();
        for (int i = 0; i < noteData.getImagePaths().size(); i++) {
            final Bitmap bitmap = BitmapFactory.decodeFile(noteData.getImagePaths().get(i));
            mStorageRef = FirebaseStorage.getInstance().getReference().child("inote-images").child(noteData.getNoteId()).child(noteData.getImageIds().get(i) + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            final boolean[] flag = {false};
            UploadTask uploadTask = mStorageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    int i = 0;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc
                    flag[0] = true;

                }
            });

        }
    }

<<<<<<< HEAD
        private void downloadImage ( final NoteData noteData) throws IOException {
            if (noteData.getImageIds() == null) {

            } else {
                for (int i = 0; i < noteData.getImageIds().size(); i++) {
                    mStorageRef = FirebaseStorage.getInstance().getReference().child("inote-images").child(noteData.getNoteId()).child(noteData.getImageIds().get(i) + ".jpg");

                    ContextWrapper cw = new ContextWrapper(mcontext);
                    final File directory = cw.getDir(noteData.getNoteId(), Context.MODE_PRIVATE);
                    final File myImageFile = new File(directory, noteData.getImageIds().get(i) + ".jpg"); // Create image file
                    if (myImageFile.exists()) {
                        String path = Long.toString(myImageFile.getTotalSpace());
                    } else {
                        //final File localFile = File.createTempFile(noteData.getImageIds().get(i), "jpg");
                        mStorageRef.getFile(myImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                String path = myImageFile.getAbsolutePath();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                }
            }

        }

        private int searchImageId (String noteId){
            for (int i = 0; i < image_note_store.size(); i++) {
                if (image_note_store.get(i).getNoteID().equals(noteId)) {
                    return i;
                }
            }

            return -1;
        }
=======
    @Override
    public void load_notes() {

    }

>>>>>>> f7ea07f8ccaf619ea068cb8881acb22091e502ae
}


/*
    private Target picassoImageTarget(final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(mcontext);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        final Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
                        //Toast.makeText(mcontext,"image saved to >>>" + myImageFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {

                    final File myImageFile = new File(directory, imageName); // Create image file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(myImageFile);
                   //     bitmap.compress(Bitmap.CompressFormat.JPEG, 25, fos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
                }
            }
        };
        return mTarget;
    }
*/









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


 /*        mDatabase_get.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                *//*for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
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
                }*//*

                    dataList.clear();

                    int count = 0;
                    int imageCount = 0;
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        NoteData note= noteDataSnapshot.getValue(NoteData.class);
                        dataList.add(note);

                        if(note.getImageIds().size() != 0) {
                        *//*int isImageAlreadyStore = searchImageId(note.getNoteId());
                        if (isImageAlreadyStore != -1) {*//*
                            try {
                                downloadImage(note);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        *//*} else {
                            ImageStoreStructure imageStoreStructure = new ImageStoreStructure(count, note.getNoteId(), null);
                            image_note_store.add(imageStoreStructure);
                            //image_note_id.add(note.getImageId());
                            try {
                                downloadImage(note);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }*//*
                        }
                    }

                    for (Consumer subject : consumers) {
                        subject.consume(dataList);
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    /*try {

     *//*for (int ii = 0; ii < noteData.getImageIds().size(); ii++) {


                    final File localFile = File.createTempFile(noteData.getImageIds().get(ii), "jpg");
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
                }*//*
            } catch (IOException e) {
                e.printStackTrace();
            }*/


//https://firebasestorage.googleapis.com/v0/b/note-40b8e.appspot.com/o/inote-images%2F-LSr-kqLGderUff2Bwhl%2F_0_-LSr-kqLGderUff2Bwhl.jpg?alt=media&token=95456721-8541-4c2b-9c00-61523db0b5d4
//noteData.setImagePaths(null);


///data/user/0/com.example.adityapatel.note/app_-LSv1LhiXH0engRq_V0w/__0-LSv1LhiXH0engRq_V0w.jpg
///data/user/0/com.example.adityapatel.note/app_-LSv1LhiXH0engRq_V0w/__0-LSv1LhiXH0engRq_V0w.jpeg
///data/user/0/com.example.adityapatel.note/app_-LSv-wy1voHLBfN1ioI7/__0-LSv-wy1voHLBfN1ioI7.jpeg
///data/user/0/com.example.adityapatel.note/app_-LSv-wy1voHLBfN1ioI7/__0-LSv-wy1voHLBfN1ioI7.jpeg

        /*mStorageRef = FirebaseStorage.getInstance().getReference().child("inote-images").child(noteData.getNoteId());
        for(int i=0;i<noteData.getImageIds().size();i++) {
            StorageReference storage = mStorageRef.child(noteData.getImageIds().get(i)+".jpg");
            final String imageid = noteData.getImageIds().get(i);
            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Picasso.with(mcontext).load(uri).into(picassoImageTarget( noteData.getNoteId(), imageid+".jpeg"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    String ec = exception.toString();

                    // Handle any errors
                }
            });

        }*/
        /*if(image_note_store.get(index).getImagePaths() != null){

            dataList.get(index).setImagePath(image_note_store.get(index).getImagePaths().get(0));
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
        }*/

