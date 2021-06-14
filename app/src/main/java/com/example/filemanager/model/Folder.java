package com.example.filemanager.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.Date;

public class Folder {
    private File file;
    private String nameFolder;
    private Context context;

    public Folder(Context context, File file, String nameFolder) {
        this.file = file;
        this.nameFolder = nameFolder;
        this.context = context;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
