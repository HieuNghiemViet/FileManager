package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.filemanager.model.Image;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class StorageActivity extends AppCompatActivity implements OnItemClickListener {
    private static final int RENAME_REQUEST_CODE = 1000;
    private static Uri extUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private ArrayList<Folder> arrayList = new ArrayList<>();
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    ArrayList<String> listPaths = new ArrayList<>();
    private Folder folderTmp;
    private TextView tv_addFolder_cancel;
    private TextView tv_addFolder_ok;
    private EditText edt_addFolder;
    private FloatingActionButton btn_add;
    private String folderName;
    private Button btn_paste;
    private TextView tv_rename_cancel;
    private TextView tv_rename_ok;
    private EditText edt_rename;

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
                createFolder(path);
            }
        }
    }

    @Override
    public void onClick(int position) {
        if (arrayList.size() > 0) {
            String path = arrayList.get(position).getFile().getAbsolutePath();
            listPaths.add(path);
            File directory = new File(path);
            File[] files = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return !pathname.isHidden();
                }
            });
            folderTmp = arrayList.get(position);
//            if (folderTmp.getNameFolder().toLowerCase().endsWith(".mp3") || folderTmp.getNameFolder().toLowerCase().endsWith(".wav")) {
//                Log.d("HieuNV", "folderTmp.getNameFolder().toLower: " + folderTmp.getNameFolder().toLowerCase().endsWith(".mp3"));
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(folderTmp.getPathFolder()));
//                intent.setDataAndType(Uri.parse(folderTmp.getPathFolder()), "audio/*");
//                startActivity(intent);

//                Intent in = new Intent();
//                in.setAction(android.content.Intent.ACTION_VIEW);
//                File file = new File(folderTmp.getPathFolder());
//                in.setDataAndType(Uri.fromFile(file), "audio/*");
//                startActivity(in);
//
//            } else if (folderTmp.getNameFolder().toLowerCase().endsWith(".mp4")) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(folderTmp.getPathFolder()));
//                intent.setDataAndType(Uri.parse(folderTmp.getPathFolder()), "video/*");
//                startActivity(intent);
//            } else if (folderTmp.getNameFolder().toLowerCase().endsWith(".jpeg") ||
//                    folderTmp.getNameFolder().toLowerCase().endsWith(".jpg") ||
//                    folderTmp.getNameFolder().toLowerCase().endsWith(".png")) {
//            } // /////

            arrayList.clear();
            if (files != null) {
                if (files.length > 0) {
                    Arrays.sort(files, new FileComparator());
                    for (int i = 0; i < files.length; i++) {
                        arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName(), files[i].getAbsolutePath()));
                        createFolder(path);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(StorageActivity.this, "Empty folder", Toast.LENGTH_LONG).show();
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
                       // copyFolder(folderTmp.getPathFolder(), );
                        btn_paste.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        cutFolder();
                        break;
                    case 2:
                        renameFolder();
                        break;
                    case 3:
                        deleteFolder();
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

    private void deleteFolder() {
        File fDelete = new File(folderTmp.getPathFolder());
        if (fDelete.exists()) {
            if (fDelete.delete()) {
                Toast.makeText(StorageActivity.this, "Folder Delete", Toast.LENGTH_LONG).show();
                getFolder();
            } else {
                Toast.makeText(StorageActivity.this, "Folder Not Delete", Toast.LENGTH_LONG).show();
            }
        }
        adapter.notifyDataSetChanged();
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
            public void onClick(View v) {
                File oldFolder = new File(folderTmp.getPathFolder());
                File newFolder = new File(folderTmp.getPathFolder(), edt_rename.toString());
                if (oldFolder.exists()) {
                     oldFolder.renameTo(newFolder);
                     Toast.makeText(StorageActivity.this, "Rename Successfully", Toast.LENGTH_LONG).show();
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

    private void cutFolder() {
    }

    private void copyFolder(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        arrayList.clear();
        if (files != null) {
            Arrays.sort(files, new FileComparator());
            for (int i = 0; i < files.length; i++) {
                arrayList.add(new Folder(StorageActivity.this, files[i], files[i].getName(), files[i].getAbsolutePath()));
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void createFolder(String path) {
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
                        folderName = edt_addFolder.getText().toString().trim();
                        File file = new File(path, folderName);
                        if (!file.exists()) {
                            file.mkdir();
                            if (file.isDirectory()) {
                                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                        //adapter.notifyDataSetChanged(); // fix adapter when create folder
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
}
