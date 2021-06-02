package com.example.filemanager.model;

import android.util.Log;

import java.io.Serializable;

public class Image implements Serializable {
    String path;
    String title;
    long size;
    long date;
    int resolution;

    public Image(String path, String title, long size, long date) {
        this.path = path;
        this.title = title;
        this.size = size;
        this.date = date;
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
}
