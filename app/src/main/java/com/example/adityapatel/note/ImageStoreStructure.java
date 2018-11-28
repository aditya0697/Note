package com.example.adityapatel.note;

public class ImageStoreStructure {
    int id;
    String noteID;
    String imagePath;

    public ImageStoreStructure(int id, String noteID, String imagePath) {
        this.id = id;
        this.noteID = noteID;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
