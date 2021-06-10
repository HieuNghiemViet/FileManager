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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //  Log.d("HieuNV", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder (StorageActivity.this, files[i], files[i].getName(), files.length));
                Log.d("HieuNV", "size: " + files.length);
                Log.d("HieuNV", "sizeFile: " + files.length);
                Log.d("HieuNV", "DATE: " + "lastModDate");
            }
        }
    }

    @Override
    public void onClick(int position) {
      //  folder = arrayList.get(position);
        String path = arrayList.get(position).getFile().getAbsolutePath();
      //  Log.d("HieuNV", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
     //   Log.d("HieuNV", "Size: "+ files.length);
        arrayList.clear();
        for (int i = 0; i < files.length; i++)
        {
            arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName() , files.length));
            Log.d("HieuNV", "FileName:" + files[i].getName());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClick(int position) {
    }
}

