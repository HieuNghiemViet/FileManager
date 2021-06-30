package com.example.filemanager.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Folder implements Serializable {
    private File file;
    private String nameFolder;
    private Context context;
    private String pathFolder;
    private boolean isSelected = false;
    private ArrayList<String> selectsPath;

    public Folder(Context context, File file, String nameFolder, String pathFolder, ArrayList<String> selectsPath) {
        this.file = file;
        this.nameFolder = nameFolder;
        this.context = context;
        this.pathFolder = pathFolder;
        this.selectsPath = selectsPath;
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

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<String> getSelectsPath() {
        return selectsPath;
    }

    public void setSelectsPath(ArrayList<String> selectsPath) {
        this.selectsPath = selectsPath;
    }
}
