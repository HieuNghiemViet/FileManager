package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StorageActivity extends AppCompatActivity implements OnItemClickListener {
    private ArrayList<Folder> arrayList = new ArrayList<>();
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    private ImageView img_back;
    String pathTemp;
    ArrayList<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        initView();
        setDataAdapter();

    }

    public void initView() {
        rcv_storage = (RecyclerView) findViewById(R.id.rcv_storage);
        img_back = (ImageView) findViewById(R.id.img_back);
    }

    public void setDataAdapter() {
        getFolder();
        adapter = new StorageAdapter(arrayList, this, this);
        rcv_storage.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rcv_storage.setLayoutManager(staggeredGridLayoutManager);

    }

    public void getFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File directory = new File(path);
        paths.add(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
            }
        }
    }

    @Override
    public void onClick(int position) {
        img_back.setVisibility(View.VISIBLE);
        if (arrayList.size() > 0) {
            String path = arrayList.get(position).getFile().getAbsolutePath();
            Log.d("HieuNV", "String path: " + path);
            pathTemp = path;
            paths.add(path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            arrayList.clear();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
                }
                adapter.notifyDataSetChanged();
                Log.d("HieuNV", "PATH: " + paths);
            }
        } else {
            Toast.makeText(this, "Folder Empty", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLongClick(int position) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ArrayList<File> files = new ArrayList<>();






//        String previousDir = "storage/emulated/0/MUSIC";
//        if (previousDir != null) {
//            Intent activityDir = new Intent(this, StorageActivity.class);
//         //   activityDir.putExtra("DIR_PATH", previousDir);
//            startActivity(activityDir);
//        }
//        finish();
    }
}
