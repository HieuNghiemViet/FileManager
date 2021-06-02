package com.example.filemanager.model;

import android.widget.ImageView;

public class Document {
    private String documentName;
    private int documentSize;
    private int documentImage;

    public Document(String documentName, int documentSize,int documentImage) {
        this.documentName = documentName;
        this.documentSize = documentSize;
        this.documentImage = documentImage;
    }


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(int documentSize) {
        this.documentSize = documentSize;
    }

    public int getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(int documentImage) {
        this.documentImage = documentImage;
    }
}
