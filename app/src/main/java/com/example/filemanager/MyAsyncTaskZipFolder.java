package com.example.filemanager;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;


public class MyAsyncTaskZipFolder extends AsyncTask<String, Void, Boolean> {

    StorageActivity storageActivity;
    private ArrayList<String> arrayListZip = new ArrayList();

    public MyAsyncTaskZipFolder(StorageActivity storageActivity) {
        this.storageActivity = storageActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(storageActivity, "Đang Xử Lí", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
//        ZipManager.zipFile();
//        ZipManager.extractFileZip();
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }


}
