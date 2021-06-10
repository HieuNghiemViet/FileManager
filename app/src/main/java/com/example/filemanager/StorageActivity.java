package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StorageActivity extends AppCompatActivity implements OnItemClickListener{
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    private ArrayList<Folder> arrayList = new ArrayList<>();
    Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        initView();
        setDataAdapter();
        //
    }

    public void initView() {
        rcv_storage = (RecyclerView) findViewById(R.id.rcv_storage);
    }

    public void setDataAdapter() {
        getFolder();
        adapter = new StorageAdapter(arrayList, this, this);
        rcv_storage.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcv_storage.setLayoutManager(staggeredGridLayoutManager);
    }



    //fix
    public void getFolder() {
        File extStorageDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        String[] fileList = extStorageDir.list();

        String pathDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String pathDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String pathPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        String pathMusic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        String pathMovie = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
 //       String pathDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_).getAbsolutePath();

        File mDCIM = new File(pathDCIM);
        File[] files = mDCIM.listFiles();
        String lastModDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date(mDCIM.lastModified()));

        //File mDownload = new File(pathDownload);
        //File[] file = mDownload.listFiles();



        for (int i = 0; i < fileList.length; i++) {
            if(files != null) {
                arrayList.add(new Folder (fileList[i], lastModDate , files.length,null));
                Log.d("HieuNV", "size: " + fileList.length);
                Log.d("HieuNV", "sizeFile: " + files.length);
                Log.d("HieuNV", "DATE: " + lastModDate);
            }
        }
    }

    @Override
    public void onClick(int position) {
      //  folder = arrayList.get(position);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
      //  Log.d("HieuNV", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
     //   Log.d("HieuNV", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
      //      Log.d("HieuNV", "FileName:" + files[i].getName());
        }
    }

    @Override
    public void onLongClick(int position) {
    }
}

