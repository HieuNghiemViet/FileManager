package com.example.filemanager.model;

public class Folder {
    private String nameFolder;
    private String dateFolder;
    private String numberFile;

    public Folder(String nameFolder, String dateFolder, String numberFile) {
        this.nameFolder = nameFolder;
        this.dateFolder = dateFolder;
        this.numberFile = numberFile;
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

    public String getNumberFile() {
        return numberFile;
    }

    public void setNumberFile(String numberFile) {
        this.numberFile = numberFile;
    }
}
