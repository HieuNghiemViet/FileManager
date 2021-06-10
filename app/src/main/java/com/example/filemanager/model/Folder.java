package com.example.filemanager.model;

import java.util.Date;

public class Folder {
    private String nameFolder;
    private String dateFolder;
    private int numberFile;
    private String pathFolder;

    public Folder(String nameFolder, String dateFolder, int numberFile , String pathFolder) {
        this.nameFolder = nameFolder;
        this.dateFolder = dateFolder;
        this.numberFile = numberFile;
        this.pathFolder = pathFolder;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public String getDateFolder() {
        return dateFolder;
    }

    public void setDateFolder(String dateFolder) {
        this.dateFolder = dateFolder;
    }

    public int getNumberFile() {
        return numberFile;
    }

    public void setNumberFile(int numberFile) {
        this.numberFile = numberFile;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }
}
