package com.example.filemanager.model;


import java.io.Serializable;

public class Image implements Serializable {
    String path;
    String title;
    long size;
    long date;
    int resolution;
    String displayName;
    Long id;
    long dateTaken;

    public Image(String path, String title, long size, long date, String displayName, Long id, Long dateTaken) {
        this.path = path;
        this.title = title;
        this.size = size;
        this.date = date;
        this.displayName = displayName;
        this.id = id;
        this.dateTaken = dateTaken;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }
}
