package com.example.adityapatel.note;

import java.util.ArrayList;
import java.util.List;

public class ImageStoreStructure {
    int id;
    String noteID;
    List<String> imagePaths = new ArrayList<>();

    public ImageStoreStructure(int id, String noteID, String imagePaths) {
        this.id = id;
        this.noteID = noteID;
        this.imagePaths.add(imagePaths);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePath(String imagePath) {
        this.imagePaths.add(imagePath);
    }
}
