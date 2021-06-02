package com.example.filemanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.filemanager.adapter.ImageAdapter;
import com.example.filemanager.callback.OnItemClickListener;
import com.example.filemanager.model.Image;


import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ImageActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Image> arrayList = new ArrayList<>();
    private ImageAdapter adapter;
    private TextView tv_info_image_cancel;
    private TextView tv_name;
    private TextView tv_path;
    private TextView tv_size;
    private TextView tv_date;
    private TextView tv_resolution;
    private TextView tv_rename_cancel;
    private TextView tv_rename_ok;
    private EditText edt_rename;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initView();
        try {
            setDataAdapter();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rcv_image);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataAdapter() throws ParseException {
        adapter = new ImageAdapter(arrayList, this, this);
        getImage();
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getImage() {
        ContentResolver contentResolver = getContentResolver();
        Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //Log.d("HieuNV", "uri: " + imgUri);

        Cursor imgCursor = contentResolver.query(imgUri, null, null, null);
        if (imgCursor != null && imgCursor.moveToFirst()) {
            int imgTitle = imgCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            int imgPath = imgCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int imgSize = imgCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int imgDate = imgCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            do {
                String currentTitle = imgCursor.getString(imgTitle);
                String currentPath = imgCursor.getString(imgPath);
                long currentSize = imgCursor.getLong(imgSize);
                long currentDate = imgCursor.getLong(imgDate);
                arrayList.add(new Image(currentPath, currentTitle, currentSize, currentDate));
//                Log.d("HieuNV", "Name: " + currentTitle); // TITLE --> Name
//                Log.d("HieuNV", "Path: " + currentPath);
            } while (imgCursor.moveToNext());
        }
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(ImageActivity.this, DetailImageActivity.class);
        Image image = arrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", image);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        Image image = arrayList.get(position);

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final String[] feature = {"Thông tin", "Chép vào", "Đổi tên", "Xóa", "Chia Sẻ"};

        myBuilder.setItems(feature, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0:
                        infoImage(Gravity.CENTER, image);
                        break;
                    case 1:
                        cutImage();
                        break;
                    case 2:
                        renameImage(Gravity.CENTER, image);
                        break;
                    case 3:
                        deleteDialog(image);
                        deleteImage(image);
                        break;
                    case 4:
                        shareImage(image);
                }
            }
        });
        myBuilder.create().show();
    }

    private void renameImage(int gravity, Image image) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rename_image);

        tv_rename_cancel = dialog.findViewById(R.id.tv_rename_huy);
        tv_rename_ok = dialog.findViewById(R.id.tv_rename_ok);
        edt_rename = dialog.findViewById(R.id.edt_rename);
        edt_rename.setText(image.getTitle());

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

        //File dir = Environment.getExternalStorageDirectory();
        //Log.d("HieuNV", "dir: " + dir);

        tv_rename_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = edt_rename.getText().toString();
//                Log.d("HieuNV", "NEW NAME:  " + newName);
//                Log.d("HieuNV", "PATH:  " + image.getPath());

//                File dir = new File(image.getPath());
//                Log.d("HieuNV", "dir: " + dir);
//                if (dir.exists()) {
//                    File from = new File(dir, image.getTitle());
//                    File to = new File(dir, "newName.jpg");
//                    if (from.exists()) {
//                        if (from.renameTo(to)) {
//                            Toast.makeText(ImageActivity.this,
//                                    "OK",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }

//                String filepath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
//                File from = new File(filepath, image.getTitle());
//                Log.d("HieuNV", "from: " + image.getTitle());
//                File to = new File(filepath, "test.jpg");
//                from.renameTo(to);

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

    private void shareImage(Image image) {

        File imgFile = new File(image.getPath());
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", imgFile);
        Log.d("HieuNV", "img: " +imgFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    private void cutImage() {
    }

    // DELETE IMAGE FIX
    private void deleteImage(Image image) {
        String path = image.getPath();
        File file = new File(path);
        // Log.d("HieuNV", " " + file.getAbsolutePath());
        try {
            // Log.d("HieuNV", "path: " + path);
            if (file.exists()) {
                // Log.d("HieuNV", "exits: true");
                file.delete();
                //Log.d("HieuNV", "deleteImage: true");
            }
        } catch (Exception e) {
            Log.d("HieuNV", " " + e);
        }
    }

    public void deleteDialog(Image image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Image")
                .setMessage("You Are OK?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteImage(image);
                        arrayList.remove(image);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void infoImage(int gravity, Image image) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_info_image);

        tv_info_image_cancel = dialog.findViewById(R.id.tv_huy);
        tv_name = dialog.findViewById(R.id.tv_nameImage);
        tv_path = dialog.findViewById(R.id.tv_pathImage);
        tv_size = dialog.findViewById(R.id.tv_sizeImage);
        tv_date = dialog.findViewById(R.id.tv_dayImage);
        tv_resolution = dialog.findViewById(R.id.tv_resolutionImage);

        tv_name.setText(image.getTitle());
        tv_path.setText(image.getPath());
        tv_size.setText(Formatter.formatShortFileSize(dialog.getContext(), image.getSize()));

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        tv_date.setText(sdf.format(image.getDate()));

        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        bitmap.getHeight();
        bitmap.getWidth();
        tv_resolution.setText(bitmap.getWidth() + " x " + bitmap.getHeight());

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

        tv_info_image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}


