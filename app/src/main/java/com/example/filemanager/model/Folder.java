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
    private int numberFile;
    private String pathFolder;
    private Context context;

    public Folder(Context context, File file, String nameFolder, int numberFile) {
        this.file = file;
        this.nameFolder = nameFolder;
        this.numberFile = numberFile;
        this.pathFolder = pathFolder;
        this.context = context;
    }

    public Long getDate() {
        Uri extUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns.DATE_MODIFIED}; // fix
        Cursor cursor = context.getContentResolver().query(extUri, projection,
                MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", new String[]{nameFolder}, null);

        assert cursor != null;
        cursor.moveToFirst();
        long currentDate = 0;
        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            currentDate = cursor.getLong(columnIndex);
            cursor.close();
            return currentDate;
        }
        return currentDate;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public int getNumberFile() {
        return numberFile;
    }

    public File getFile(){
        return  file;
    }
    public void setNumberFile(int numberFile) {
        this.numberFile = numberFile;
    }

}
