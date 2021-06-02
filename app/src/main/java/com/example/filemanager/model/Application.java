package com.example.filemanager.model;

import android.graphics.drawable.Drawable;

public class Application {
    Drawable imageApp;
    String nameApp;
    String packageApp;
    long sizeApp;

    public Application(Drawable imageApp, String nameApp, String packageApp,long sizeApp) {
        this.imageApp = imageApp;
        this.nameApp = nameApp;
        this.packageApp = packageApp;
        this.sizeApp = sizeApp;
    }


    public Drawable getImageApp() {
        return imageApp;
    }

    public void setImageApp(Drawable imageApp) {
        this.imageApp = imageApp;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getPackageApp() {
        return packageApp;
    }

    public void setPackageApp(String packageApp) {
        this.packageApp = packageApp;
    }

    public long getSizeApp() {
        return sizeApp;
    }

    public void setSizeApp(long sizeApp) {
        this.sizeApp = sizeApp;
    }
}
