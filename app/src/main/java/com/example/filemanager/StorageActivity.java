package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StorageActivity extends AppCompatActivity implements OnItemClickListener {
    private ArrayList<Folder> arrayList = new ArrayList<>();
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    ArrayList<String> listPaths = new ArrayList<>();

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
        listPaths.clear();
        getFolder();
        adapter = new StorageAdapter(arrayList, this, this);
        rcv_storage.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcv_storage.setLayoutManager(staggeredGridLayoutManager);
    }

    public void getFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File directory = new File(path);
        listPaths.add(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
            }
        }
    }

    @Override
    public void onClick(int position) {
        if (arrayList.size() > 0) {
            String path = arrayList.get(position).getFile().getAbsolutePath();
            listPaths.add(path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            arrayList.clear();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
                }
                adapter.notifyDataSetChanged();
//                if (arrayList.get(position).getFile().getAbsolutePath().toLowerCase().endsWith(".mp3")) {
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse(arrayList.get(position).getFile().getAbsolutePath()), "audio/*");
//                    startActivity(intent);
//                }

            } else {
                Toast.makeText(this, "File Empty", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onLongClick(int position) {
    }

    @Override
    public void onBackPressed() {
        File[] files;
        if (listPaths.size() == 1) {
            super.onBackPressed();
            return;
        }
        Log.d("HieuNV", "listPath: " + listPaths);
        File directory = new File(listPaths.get(listPaths.size() - 2));
        files = directory.listFiles(); // get all file children
        listPaths.remove(listPaths.size() - 1);
        arrayList.clear();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
            }
            adapter.notifyDataSetChanged();
        }
    }
}
