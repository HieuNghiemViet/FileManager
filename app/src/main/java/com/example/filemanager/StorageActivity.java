package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.adapter.SongAdapter;
import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.function.LongFunction;

public class StorageActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    private ArrayList<Folder> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        initView();
        setDataAdapter();
    }

    public void initView() {
        rcv_storage = (RecyclerView) findViewById(R.id.rcv_storage);
    }

    public void setDataAdapter() {
        getFolder();
        adapter = new StorageAdapter(arrayList,this,this);
        rcv_storage.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcv_storage.setLayoutManager(staggeredGridLayoutManager);
    }

    public void getFolder() {
        File extStorageDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        String[] fileList = extStorageDir.list();
        for (int i = 0; i < fileList.length; i++) {
            arrayList.add(new Folder(fileList[i].toString()));
        }
    }

    @Override
    public void onClick(int position) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        Log.d("HieuNV", " " +Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() );
        String m = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Log.d("HieuNV", " " + m);
    }

    @Override
    public void onLongClick(int position) {
    }
}

