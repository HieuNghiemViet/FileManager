package com.example.filemanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.adapter.StorageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Folder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class StorageActivity extends AppCompatActivity implements OnItemClickListener {

    private static final int RENAME_REQUEST_CODE = 1000;
    private static final int OK_REQUEST_CODE = 2000;
    private static final int CREATE_REQUEST_CODE = 3000;
    private static Uri extUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private ArrayList<Folder> arrayList = new ArrayList<>();
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    ArrayList<String> listPaths = new ArrayList<>();
    private Folder folderTmp;
    private Folder copyTmp;
    private TextView tv_addFolder_cancel;
    private TextView tv_addFolder_ok;
    private EditText edt_addFolder;
    private FloatingActionButton btn_add;
    private String folderName;
    private Button btn_paste;
    private Button btn_cancel;
    private TextView tv_rename_cancel;
    private TextView tv_rename_ok;
    private EditText edt_rename;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        initView();
        setDataAdapter();
    }

    public void initView() {
        rcv_storage = (RecyclerView) findViewById(R.id.rcv_storage);
        btn_add = (FloatingActionButton) findViewById(R.id.fabAdd);
        btn_paste = (Button) findViewById(R.id.btn_paste);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        emptyView = (TextView) findViewById(R.id.empty_view);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(StorageActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_add_folder);

                tv_addFolder_cancel = dialog.findViewById(R.id.tv_addFolder_huy);
                tv_addFolder_ok = dialog.findViewById(R.id.tv_addFolder_ok);
                edt_addFolder = dialog.findViewById(R.id.edt_addFolder);

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                window.setAttributes(windowAttributes);
                dialog.show();

                tv_addFolder_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createFolder();
                        dialog.dismiss();
                    }
                });
                tv_addFolder_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
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
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        if (files != null) {
            Arrays.sort(files, new FileComparator());
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName(), files[i].getAbsolutePath()));
            }
        }
    }

    private void createFolder() {
//        if (ActivityCompat.checkSelfPermission(StorageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
        folderName = edt_addFolder.getText().toString().trim();
        File file = new File(listPaths.get(listPaths.size() - 1), folderName);
        if (!file.exists()) {
            if (file.mkdir()) {
                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                repaintUI(listPaths.get(listPaths.size() - 1));
            }
        }
//        } else {
//            ActivityCompat.requestPermissions(StorageActivity.this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    CREATE_REQUEST_CODE);
//        }
    }

    @Override
    public void onClick(int position) {
        if (!arrayList.isEmpty()) {
            folderTmp = arrayList.get(position);
            listPaths.add(arrayList.get(position).getFile().getAbsolutePath());
            //fix image when repaintUI
            if (folderTmp.getNameFolder().toLowerCase().endsWith(".mp3") || folderTmp.getNameFolder().toLowerCase().endsWith(".wav")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(folderTmp.getPathFolder()));
                intent.setDataAndType(Uri.parse(folderTmp.getPathFolder()), "audio/*");
                startActivity(intent);

            } else if (folderTmp.getNameFolder().toLowerCase().endsWith(".mp4")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(folderTmp.getPathFolder()));
                intent.setDataAndType(Uri.parse(folderTmp.getPathFolder()), "video/*");
                startActivity(intent);
            } else if (folderTmp.getNameFolder().toLowerCase().endsWith(".jpeg") ||
                    folderTmp.getNameFolder().toLowerCase().endsWith(".jpg") ||
                    folderTmp.getNameFolder().toLowerCase().endsWith(".png")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(folderTmp.getPathFolder()));
                intent.setDataAndType(Uri.parse(folderTmp.getPathFolder()), "image/*");
                startActivity(intent);
            } else {
                repaintUI(arrayList.get(position).getFile().getAbsolutePath());
            }
        }
    }

    private void repaintUI(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        arrayList.clear();
        if (files != null) {
            if (files.length > 0) {
                Arrays.sort(files, new FileComparator());
                for (int i = 0; i < files.length; i++) {
                    arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName(), files[i].getAbsolutePath()));
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if (arrayList.isEmpty()) {
            rcv_storage.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            rcv_storage.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLongClick(int position) {
        folderTmp = arrayList.get(position);
        copyTmp = arrayList.get(position);
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final String[] feature = {"Copy", "Move", "Rename", "Delete"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
//                if (ActivityCompat.checkSelfPermission(StorageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
                switch (position) {
                    case 0: // copy
                        btn_paste.setVisibility(View.VISIBLE);
                        btn_paste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                copyFileOrDirectory(copyTmp.getPathFolder(), listPaths.get(listPaths.size() - 1));
                                repaintUI(listPaths.get(listPaths.size() - 1));
                                Toast.makeText(StorageActivity.this, "Successfully", Toast.LENGTH_LONG).show();
                                btn_paste.setVisibility(View.INVISIBLE);
                                btn_cancel.setVisibility(View.INVISIBLE);
                            }
                        });
                        btn_cancel.setVisibility(View.VISIBLE);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_paste.setVisibility(View.INVISIBLE);
                                btn_cancel.setVisibility(View.INVISIBLE);
                            }
                        });
                        break;
                    case 1: // Move
                        btn_paste.setVisibility(View.VISIBLE);
                        btn_paste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cutFolder(copyTmp.getPathFolder(), listPaths.get(listPaths.size() - 1));
                                Toast.makeText(StorageActivity.this, "Move Successfully", Toast.LENGTH_LONG).show();
                                btn_paste.setVisibility(View.INVISIBLE);
                                btn_cancel.setVisibility(View.INVISIBLE);
                            }
                        });
                        btn_cancel.setVisibility(View.VISIBLE);
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                btn_paste.setVisibility(View.INVISIBLE);
                                btn_cancel.setVisibility(View.INVISIBLE);
                            }
                        });
                        break;
                    case 2: // Rename
                        renameFolder();
                        break;
                    case 3: // Delete
                        if (deleteRecursive(new File(folderTmp.getPathFolder()))) {
                            Toast.makeText(StorageActivity.this, "Delete Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(StorageActivity.this, "Delete Not Successfully", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
//                } else {
//                    ActivityCompat.requestPermissions(StorageActivity.this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            OK_REQUEST_CODE);
//                }
            }
        });
        myBuilder.create().show();
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private void cutFolder(String src, String dst) {
        copyFileOrDirectory(src, dst);
        repaintUI(dst);
        deleteOnMove(new File(src));
    }

    boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        if (fileOrDirectory.delete()) {
            int index = findDirectoryByName(fileOrDirectory.getName());
            if (index != -1) {
                arrayList.remove(index);
                adapter.notifyDataSetChanged();
            }
            return true;
        } else {
            return false;
        }
    }

    public void deleteOnMove(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        fileOrDirectory.delete();
    }

    public int findDirectoryByName(String name) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getNameFolder().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private void renameFolder() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rename);

        tv_rename_cancel = dialog.findViewById(R.id.tv_rename_huy);
        tv_rename_ok = dialog.findViewById(R.id.tv_rename_ok);
        edt_rename = dialog.findViewById(R.id.edt_rename);
        edt_rename.setText(folderTmp.getNameFolder());

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        dialog.show();

        tv_rename_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // rename đẻ ra nhieu ảnh giống nhau khác tên
                File oldFolder = new File(listPaths.get(listPaths.size() - 1), folderTmp.getNameFolder());
                File newFolder = new File(listPaths.get(listPaths.size() - 1), edt_rename.getText().toString());
                if (oldFolder.exists()) {
                    if (oldFolder.renameTo(newFolder)) {
//                        MediaScannerConnection.scanFile(StorageActivity.this,
//                                new String[]{newFolder.toString()},
//                                null, null);
                        Toast.makeText(StorageActivity.this, "Rename Successfully", Toast.LENGTH_LONG).show();
                        repaintUI(listPaths.get(listPaths.size() - 1));
                    } else {
                        Toast.makeText(StorageActivity.this, "Rename Not Successfully ", Toast.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
            }
        });

        tv_rename_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        File[] files;
        if (listPaths.size() == 1) {
            super.onBackPressed();
            return;
        }
        File directory = new File(listPaths.get(listPaths.size() - 2));
        files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        listPaths.remove(listPaths.size() - 1);
        repaintUI(directory.getAbsolutePath());
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CREATE_REQUEST_CODE && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//            createFolder();
//        }
//    }
}

