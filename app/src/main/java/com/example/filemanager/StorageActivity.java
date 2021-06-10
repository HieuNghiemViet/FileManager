package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.UUID;
import java.util.function.LongFunction;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StorageActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    private ArrayList<Folder> arrayList = new ArrayList<>();

    public StorageActivity() throws IOException {
    }

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
        adapter = new StorageAdapter(arrayList, this, this);
        rcv_storage.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcv_storage.setLayoutManager(staggeredGridLayoutManager);
    }

    //fix
    public void getFolder() {
        File extStorageDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        String[] fileList = extStorageDir.list();
        for (int i = 0; i < fileList.length; i++) {
            arrayList.add(new Folder(fileList[i], null , null));
        }
    }

    @Override
    public void onClick(int position) {
        String M = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        String INNER_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("HieuNV" , " " + INNER_STORAGE);
        Log.d("HieuNV" , "m: " + M);

        File storage= new File(System.getenv("INTERNAL_STORAGE"));
        Log.d("HieuNV", "sto: " + storage);

    }

    @Override
    public void onLongClick(int position) {
    }
}

