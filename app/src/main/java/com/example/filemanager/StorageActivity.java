package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;
import com.example.filemanager.model.Image;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class StorageActivity extends AppCompatActivity implements OnItemClickListener {
    private static Uri extUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private ArrayList<Folder> arrayList = new ArrayList<>();
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    ArrayList<String> listPaths = new ArrayList<>();
    private Folder folderTmp;

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
        Log.d("HieuNV", " path: " + path);
        File directory = new File(path);
        listPaths.add(path);
        File[] files = directory.listFiles();
        Arrays.sort(files, new FileComparator());
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
            Arrays.sort(files, new FileComparator());


            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
                }

                adapter.notifyDataSetChanged();
//                if (arrayList.get(position).getFile().getAbsolutePath().toLowerCase().endsWith(".mp3")) {
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);w
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
        folderTmp = arrayList.get(position);
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final String[] feature = {"Copy", "Move", "Rename", "Delete", "Show hidden files"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        copyFolder(Gravity.CENTER, folderTmp);
                        break;
                    case 1:
                        cutFolder();
                        break;
                    case 2:
                        renameFolder(Gravity.CENTER, folderTmp);
                        break;
                    case 3:
                        deleteDialog(folderTmp);
                        break;
                    case 4:
                        showHiddenFiles();
                        break;
                }
            }
        });
        myBuilder.create().show();
    }

    private void showHiddenFiles() {
    }

    private void deleteDialog(Folder folderTmp) {
    }

    private void renameFolder(int gravity, Folder folderTmp) {

    }

    private void cutFolder() {
    }

    private void copyFolder(int center, Folder folderTmp) {
    }

    @Override
    public void onBackPressed() {
        File[] files;
        if (listPaths.size() == 1) {
            super.onBackPressed();
            return;
        }
        File directory = new File(listPaths.get(listPaths.size() - 2));
        files = directory.listFiles(); // get all file children
        listPaths.remove(listPaths.size() - 1);
        arrayList.clear();
        Arrays.sort(files, new FileComparator());
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName()));
            }
            adapter.notifyDataSetChanged();
        }
    }

}
