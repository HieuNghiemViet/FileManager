package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class StorageActivity extends AppCompatActivity implements OnItemClickListener, CallBackZipListener {
    private static final int RENAME_REQUEST_CODE = 1000;
    private static final int DELETE_REQUEST_CODE = 2000;
    private static Uri extStorageUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
    private ArrayList<Folder> arrayList = new ArrayList<>();
    private RecyclerView rcv_storage;
    private StorageAdapter adapter;
    public static ArrayList<String> listPaths = new ArrayList<>();
    private Folder folderTmp;
    private Folder copyTmp;
    private Folder infoTmp;
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
    private ZipManager zipManager;
    public EditText edt_create_name_folder_zip;
    private TextView tv_create_folder_zip_cancel;
    private TextView tv_create_folder_zip_ok;
    private TextView tv_name_storage;
    private TextView tv_path_storage;
    private TextView tv_size_storage;
    private TextView tv_cancel_storage;
    private TextView tv_type_storage;
    private LinearLayout lnl_menu;
    private ImageView img_info;
    private ImageView img_copy;
    private ImageView img_move;
    private ImageView img_delete;
    private ImageView img_rename;
    private ImageView img_zip;
    private ImageView img_unzip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        zipManager = new ZipManager(this, this);
        initView();
        setDataAdapter();
    }

    public void initView() {
        rcv_storage = (RecyclerView) findViewById(R.id.rcv_storage);
        btn_add = (FloatingActionButton) findViewById(R.id.fabAdd);
        btn_paste = (Button) findViewById(R.id.btn_paste);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        emptyView = (TextView) findViewById(R.id.empty_view);
        lnl_menu = (LinearLayout) findViewById(R.id.lnl_menu);

        img_info = (ImageView) findViewById(R.id.img_info);
        img_copy = (ImageView) findViewById(R.id.img_copy);
        img_move = (ImageView) findViewById(R.id.img_move);
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_rename = (ImageView) findViewById(R.id.img_rename);
        img_zip = (ImageView) findViewById(R.id.img_zip);
        img_unzip = (ImageView) findViewById(R.id.img_unzip);

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
        rcv_storage.setHasFixedSize(true);
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
        folderName = edt_addFolder.getText().toString().trim();
        if (folderName.toLowerCase().endsWith(".jpeg") ||
                folderName.toLowerCase().endsWith(".jpg") ||
                folderName.toLowerCase().endsWith(".png") ||
                folderName.toLowerCase().endsWith(".mp3") ||
                folderName.toLowerCase().endsWith(".wav") ||
                folderName.toLowerCase().endsWith(".pdf") ||
                folderName.toLowerCase().endsWith(".mp4")) {
            Toast.makeText(StorageActivity.this, "Cannot Create Folder", Toast.LENGTH_LONG).show();
        } else {
            File file = new File(listPaths.get(listPaths.size() - 1), folderName);
            if (!file.exists()) {
                if (file.mkdir()) {
                    Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                    repaintUI(listPaths.get(listPaths.size() - 1));
                }
            }
        }
    }

    @Override
    public void onClick(int position) {
        if (!arrayList.isEmpty()) {
            folderTmp = arrayList.get(position);
            File file = new File(folderTmp.getPathFolder());
            if (file.isDirectory()) {
                listPaths.add(arrayList.get(position).getFile().getAbsolutePath());
                repaintUI(arrayList.get(position).getFile().getAbsolutePath());
            } else {
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
                }
            }
        }
    }

    public void repaintUI(String path) {
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
        adapter.resetStage();
        btn_add.setVisibility(View.VISIBLE);
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
        infoTmp = arrayList.get(position);
        folderTmp = arrayList.get(position);
        copyTmp = arrayList.get(position);

        if (adapter.selectListPath.size() > 0) {
            lnl_menu.setVisibility(View.VISIBLE);
            btn_add.setVisibility(View.INVISIBLE);
        } else {
            lnl_menu.setVisibility(View.INVISIBLE);
            btn_add.setVisibility(View.VISIBLE);
        }

        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(Gravity.CENTER, infoTmp);
                lnl_menu.setVisibility(View.INVISIBLE);
                adapter.changeBackGroundItems(false);
                adapter.selectListPath.clear();
            }
        });

        img_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_paste.setVisibility(View.VISIBLE);
                adapter.changeBackGroundItems(false);
                btn_paste.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < adapter.selectListPath.size(); i++) {
                            copyFileOrDirectory(adapter.selectListPath.get(i), listPaths.get(listPaths.size() - 1));
                        }
                        adapter.selectListPath.clear();
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
                        adapter.changeBackGroundItems(false);
                        btn_cancel.setVisibility(View.INVISIBLE);
                    }
                });
                lnl_menu.setVisibility(View.INVISIBLE);
            }
        });

        img_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_paste.setVisibility(View.VISIBLE);
                adapter.changeBackGroundItems(false);
                btn_paste.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //cutFolder(copyTmp.getPathFolder(), listPaths.get(listPaths.size() - 1));
                        for (int i = 0; i < adapter.selectListPath.size(); i++) {
                            cutFolder(adapter.selectListPath.get(i), listPaths.get(listPaths.size() - 1));
                        }
                        adapter.selectListPath.clear();
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
                        adapter.changeBackGroundItems(false);
                    }
                });
                lnl_menu.setVisibility(View.INVISIBLE);
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < adapter.selectListPath.size(); i++) {
                    delete(adapter.selectListPath.get(i));
                }
                lnl_menu.setVisibility(View.INVISIBLE);
                adapter.selectListPath.clear();
                adapter.changeBackGroundItems(false);
                repaintUI(listPaths.get(listPaths.size() - 1));
            }
        });

        img_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameFolder();
                adapter.changeBackGroundItems(false);
                adapter.selectListPath.clear();
                lnl_menu.setVisibility(View.INVISIBLE);
            }
        });

        img_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog create folder zip
                final Dialog dialog = new Dialog(StorageActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_create_folder_zip);

                tv_create_folder_zip_cancel = dialog.findViewById(R.id.tv_create_folder_zip_cancel);
                tv_create_folder_zip_ok = dialog.findViewById(R.id.tv_create_folder_zip_ok);
                edt_create_name_folder_zip = dialog.findViewById(R.id.edt_create_name_folder_zip);
                edt_create_name_folder_zip.setText(folderTmp.getNameFolder());

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                window.setAttributes(windowAttributes);
                dialog.show();

                tv_create_folder_zip_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        folderName = edt_create_name_folder_zip.getText().toString().trim();
                        if (isEmptyString(folderName)) {
                            Toast.makeText(StorageActivity.this, "Name Must Not Be Left Blank", Toast.LENGTH_LONG).show();
                        } else {
                            //FIX logic
                            File file = new File(listPaths.get(listPaths.size() - 1), folderName);
                            if (!file.exists()) {
                                if (file.mkdir()) {
                                    for (int i = 0; i < adapter.selectListPath.size(); i++) {
                                        copyFileOrDirectory(adapter.selectListPath.get(i), file.getAbsolutePath());

                                    }
                                }
                            }
                            zipManager.zipFile(new Folder(StorageActivity.this, file.getAbsoluteFile(), file.getName(), file.getAbsolutePath()));
                            adapter.changeBackGroundItems(false);
                            lnl_menu.setVisibility(View.INVISIBLE);
                            adapter.selectListPath.clear();
                            dialog.dismiss();
                        }
                    }
                });

                tv_create_folder_zip_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.selectListPath.clear();
                        adapter.changeBackGroundItems(false);
                        dialog.dismiss();
                    }
                });
            }
        });

        img_unzip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipManager.unZipFile(folderTmp, listPaths.get(listPaths.size() - 1));
                adapter.selectListPath.clear();
                lnl_menu.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void OnZipComplete(String path) {
        delete(path);
        repaintUI(listPaths.get(listPaths.size() - 1));
    }


    @Override
    public void OnUnZipComplete() {

        repaintUI(listPaths.get(listPaths.size() - 1));
    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {
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
                MediaScannerConnection.scanFile(StorageActivity.this, new String[]{srcDir, dstDir},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
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
        MediaScannerConnection.scanFile(this, new String[]{src, dst},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private boolean deleteRecursive(File fileOrDirectory) {
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

    public void delete(String path) {
        File fileCheck = new File(path);
        if (fileCheck.isDirectory() || path.toLowerCase().endsWith(".zip")) {
            deleteRecursive(fileCheck);
            MediaScannerConnection.scanFile(StorageActivity.this, new String[]{path},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            Toast.makeText(StorageActivity.this, "Delete Successfully", Toast.LENGTH_LONG).show();
        } else {
            if (!deleteFileStorageUsingPath(StorageActivity.this, path)) {
                deleteRecursive(fileCheck);
            }
            Toast.makeText(StorageActivity.this, "Delete Successfully", Toast.LENGTH_LONG).show();
            MediaScannerConnection.scanFile(StorageActivity.this, new String[]{path},
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

    public boolean deleteFileStorageUsingPath(Context context, String path) {
        Uri uri = getIdStorageFromPath(path);
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            String[] selectionArgsPdf = new String[]{path};
            try {
                if (resolver.delete(uri, MediaStore.Files.FileColumns.DATA + "=?", selectionArgsPdf) > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public Uri getIdStorageFromPath(String path) {
        String[] projection;
        projection = new String[]{MediaStore.Files.FileColumns._ID};
        Cursor cursor = getContentResolver().query(extStorageUri, projection,
                MediaStore.Files.FileColumns.DATA + " LIKE ?", new String[]{path}, null);

        assert cursor != null;
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            long fileId = cursor.getLong(columnIndex);

            cursor.close();
            return Uri.parse(extStorageUri.toString() + "/" + fileId);
        }
        return null;
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

    private void showInfo(int gravity, Folder infoTmp) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_main_storage);

        tv_cancel_storage = dialog.findViewById(R.id.tv_cancel_main_storage);
        tv_name_storage = dialog.findViewById(R.id.tv_name_main_storage);
        tv_path_storage = dialog.findViewById(R.id.tv_path_main_storage);
        tv_size_storage = dialog.findViewById(R.id.tv_size_main_storage);
        tv_type_storage = dialog.findViewById(R.id.tv_type_main_storage);

        tv_name_storage.setText(infoTmp.getNameFolder());
        tv_path_storage.setText(infoTmp.getPathFolder());

        File file = new File(infoTmp.getPathFolder());

        //Size
        tv_size_storage.setText(Formatter.formatFileSize(dialog.getContext(), zipManager.getFolderSize(file)));

        //Type
        if (file.isDirectory()) {
            tv_type_storage.setText("Folder");
        } else {
            tv_type_storage.setText("File");
        }

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        tv_cancel_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                File oldFolder = new File(listPaths.get(listPaths.size() - 1), folderTmp.getNameFolder());
                File newFolder = new File(listPaths.get(listPaths.size() - 1), edt_rename.getText().toString());
                if (oldFolder.exists()) {
                    if (oldFolder.renameTo(newFolder)) {
                        if (!isEmptyString(edt_rename.getText().toString())) {
                            Toast.makeText(StorageActivity.this, "Rename Successfully", Toast.LENGTH_LONG).show();
                            deleteFileStorageUsingPath(StorageActivity.this, oldFolder.getPath());
                            repaintUI(listPaths.get(listPaths.size() - 1));
                            MediaScannerConnection.scanFile(StorageActivity.this, new String[]{listPaths.get(listPaths.size() - 1), listPaths.get(listPaths.size() - 1)},
                                    null, new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String path, Uri uri) {
                                        }
                                    });
                        }
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

    public static boolean isEmptyString(String text) {
        return text == null || text.trim().equals("") || text.trim().length() <= 0;
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
        lnl_menu.setVisibility(View.INVISIBLE);
        repaintUI(directory.getAbsolutePath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                deleteFileStorageUsingPath(StorageActivity.this, folderTmp.getPathFolder());
            }
        }
        if ((requestCode == RENAME_REQUEST_CODE)) {
            if (resultCode == Activity.RESULT_OK) {
                renameFolder();
            }
        }
    }
}

